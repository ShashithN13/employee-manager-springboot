package com.example.employee_manager.service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Retrieve all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Retrieve an employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // Add or update an employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Delete an employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Check whether an email already exists
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    // Find an employee by email
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
}

