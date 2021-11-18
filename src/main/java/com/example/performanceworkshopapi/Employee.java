package com.example.performanceworkshopapi;
import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class Employee {

    private @Id @GeneratedValue Long id;
    private String firstName;
    private String lastName;
    private String role;
    private byte[] mem;

    Employee() {}

    Employee(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
    Employee(String name, String role) {
        this.setName(name);
        this.role = role;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public void setName(String name) {
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getRole() {
        return this.role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Employee))
            return false;
        Employee employee = (Employee) o;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.firstName, employee.firstName)
                && Objects.equals(this.lastName, employee.lastName) && Objects.equals(this.role, employee.role);
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.role);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
                + '\'' + ", role='" + this.role + '\'' + '}';
    }

    public void calculate() {
        //calculate
        int min = 100000;
        int max = (int) (min * 1.2);
        int n = (int) ((Math.random() * (max - min)) + min);
        //System.out.println(n + " is prime ? " +isPrime(n));
        sort();


    }

    private void sort() {
        int min = 500000;
        int max = (int) (min * 1.4);
        int n = (int) ((Math.random() * (max - min)) + min);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i<=n; i++) {
            list.add((int) ((Math.random() * (max - min)) + min));
        }
        Collections.sort(list);
        System.out.println(n);
        mem = new byte[min*100];
    }

    private boolean isPrime(int n) {
        if (n<=1)
            return false;
        if (n==2)
            return true;
        for (int i = 2; i<=n; i++) {
            if (n%i==0)
                return false;
        }
        return true;

    }
}