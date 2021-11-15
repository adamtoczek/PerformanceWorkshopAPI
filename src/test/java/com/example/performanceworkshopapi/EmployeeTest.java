package com.example.performanceworkshopapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EmployeeTest {
    @Test
    void employeesShouldBeEqual(){
        Employee em1 = new Employee("Firstname","LastName", "Role");
        Employee em2 = new Employee("Firstname","LastName", "Role");
        em2.setId(em1.getId());
        assertEquals(em1, em2);
    }

    @Test
    void sameEmployeesShouldBeEqual(){
        Employee em1 = new Employee("Firstname","LastName", "Role");
        assertEquals(em1, em1);
    }

    @Test
    void employeeShouldNotEqualString(){
        Employee em1 = new Employee("Firstname","LastName", "Role");
        assertNotEquals("Firstname LastName", em1);
    }
}
