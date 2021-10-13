package com.example.performanceWorkshopAPI;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.performanceWorkshopAPI.xrfToken.XRFToken;

import com.example.performanceWorkshopAPI.xrfToken.XRFTokenMissingException;
import com.example.performanceWorkshopAPI.xrfToken.XRFTokenRepository;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.performanceWorkshopAPI.xrfToken.XRFToken.checkToken;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;
    private final XRFTokenRepository tokenRepo;

    EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler, XRFTokenRepository tokenRepo) {

        this.repository = repository;
        this.assembler = assembler;
        this.tokenRepo = tokenRepo;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all(@RequestHeader("xrf-token") String headerToken) {
        checkToken(headerToken, tokenRepo);

        List<EntityModel<Employee>> employees = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all("")).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee, @RequestHeader("xrf-token") String headerToken) {
        checkToken(headerToken, tokenRepo);
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }
    // Single item

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id, @RequestHeader("xrf-token") String headerToken) {
        checkToken(headerToken, tokenRepo);
        Employee employee = repository.findById(id) //
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id, @RequestHeader("xrf-token") String headerToken) {
        checkToken(headerToken, tokenRepo);
        Employee updatedEmployee = repository.findById(id) //
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                }) //
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id, @RequestHeader("xrf-token") String headerToken) {
        checkToken(headerToken, tokenRepo);
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}