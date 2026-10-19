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
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // READ: Display all employees
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute(
            "employees",
            employeeService.getAllEmployees()
        );
        return "employees/list";
    }

    // CREATE: Display the add employee form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/form";
    }

    // CREATE / UPDATE: Save the submitted employee
    @PostMapping("/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result) {

        if (result.hasErrors()) {
            return "employees/form";
        }

        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // UPDATE: Display the edit employee form
    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        return employeeService.getEmployeeById(id)
            .map(employee -> {
                model.addAttribute("employee", employee);
                return "employees/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute(
                    "error",
                    "Employee not found"
                );
                return "redirect:/employees";
            });
    }

    // DELETE: Remove an employee
    @PostMapping("/delete/{id}")
    public String deleteEmployee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        if (employeeService.getEmployeeById(id).isPresent()) {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute(
                "success",
                "Employee deleted successfully"
            );
        } else {
            redirectAttributes.addFlashAttribute(
                "error",
                "Employee not found"
            );
        }

        return "redirect:/employees";
    }
}

