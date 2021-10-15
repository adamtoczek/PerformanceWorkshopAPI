package com.example.performanceWorkshopAPI;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class EmployeeTest {
    @Test
    public void employeesShouldBeEqual(){
        Employee em1 = new Employee("Firstname","LastName", "Role");
        Employee em2 = new Employee("Firstname","LastName", "Role");
        em2.setId(em1.getId());
        assertEquals(em1, em2);
    }


}
