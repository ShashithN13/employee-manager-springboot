package com.example.employee_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @Positive(message = "Salary must be greater than zero")
    private double salary;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[0-9+() -]{7,20}$",
        message = "Enter a valid phone number"
    )
    private String phone;
}
