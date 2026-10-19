
package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Display the employee form and employee table
    @GetMapping({"/", "/employees"})
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            Model model) {

        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("employees",
                    employeeService.searchEmployees(keyword));
        } else {
            model.addAttribute("employees",
                    employeeService.getAllEmployees());
        }

        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new Employee());
        }

        return "employees";
    }

    // Create a new employee
    @PostMapping("/employees")
    public String addEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (employeeService.emailExists(employee.getEmail())) {
            result.rejectValue(
                    "email", "duplicate",
                    "This email is already registered");
        }

        if (result.hasErrors()) {
            model.addAttribute("employees",
                    employeeService.getAllEmployees());
            return "employees";
        }

        employeeService.saveEmployee(employee);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee saved successfully!");

        return "redirect:/employees";
    }

    // Show the edit form
    @GetMapping("/employees/{id}/edit")
    public String editEmployee(
            @PathVariable Long id, Model model,
            RedirectAttributes redirectAttributes) {

        return employeeService.getEmployeeById(id)
                .map(employee -> {
                    model.addAttribute("employee", employee);
                    return "edit-employee";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "errorMessage", "Employee not found");
                    return "redirect:/employees";
                });
    }

    // Update an existing employee
    @PostMapping("/employees/{id}/update")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        var existing = employeeService.getEmployeeById(id);

        if (existing.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Employee not found");
            return "redirect:/employees";
        }

        if (employeeService.emailExistsForOtherEmployee(
                employee.getEmail(), id)) {
            result.rejectValue(
                    "email", "duplicate",
                    "This email is already registered");
        }

        if (result.hasErrors()) {
            employee.setId(id);
            model.addAttribute("employee", employee);
            return "edit-employee";
        }

        employee.setId(id);
        employeeService.saveEmployee(employee);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee updated successfully!");

        return "redirect:/employees";
    }

    // Delete an employee
    @PostMapping("/employees/{id}/delete")
    public String deleteEmployee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        if (employeeService.getEmployeeById(id).isPresent()) {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Employee deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Employee not found");
        }

        return "redirect:/employees";
    }
}
