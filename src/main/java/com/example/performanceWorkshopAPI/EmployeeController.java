package com.example.performanceWorkshopAPI;

import java.util.List;
import java.util.stream.Collectors;
import com.example.performanceWorkshopAPI.xrfToken.XRFTokenRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
    CollectionModel<EntityModel<Employee>> all(@RequestHeader("xrf-token") String headerToken
            , @RequestParam(required = false) String lastName
            , @RequestParam(required = false) String firstName) {
        checkToken(headerToken, tokenRepo);
        List<EntityModel<Employee>> employees;
        if (lastName!=null || firstName!=null) {
            Employee employee = new Employee();
            employee.setLastName(lastName);
            employee.setFirstName(firstName);
            ExampleMatcher matcher = ExampleMatcher.matchingAny()
                    .withIncludeNullValues()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
            Example<Employee> example = Example.of(employee, matcher);
            employees = repository.findAll(example).stream() //
                    .map(assembler::toModel) //
                    .collect(Collectors.toList());
        }
        else
            employees = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all("",lastName, firstName)).withSelfRel().expand());
    }

    @PostMapping("/employees")
    EntityModel<Employee> newEmployee(@RequestBody Employee newEmployee, @RequestHeader("xrf-token") String headerToken) {
        checkToken(headerToken, tokenRepo);
        Employee employee = repository.save(newEmployee);
        return assembler.toModel(employee);
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