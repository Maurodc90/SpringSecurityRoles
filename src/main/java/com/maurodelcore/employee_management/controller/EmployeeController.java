package com.maurodelcore.employee_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the three pages of the employee management app: a public home
 * page, an employee page and an admin page. Access to each page is
 * controlled by the security rules in {@code EmployeeSecurity}, not here.
 */
@RestController
public class EmployeeController {

    /**
     * Returns the employee page, reachable only by users with the EMPLOYEE role.
     *
     * @return the text of the employees page
     */
    @GetMapping("/employees")
    public String getEmployees() {
        return "Employees Page";
    }

    /**
     * Returns the admin page, reachable only by users with the ADMIN role.
     *
     * @return the text of the admin page
     */
    @GetMapping("/admin")
    public String getAdmin() {
        return "Admin Page";
    }

    /**
     * Returns the public home page, reachable without logging in.
     *
     * @return the text of the home page
     */
    @GetMapping("/home")
    public String getHome() {
        return "Home Page";
    }
}