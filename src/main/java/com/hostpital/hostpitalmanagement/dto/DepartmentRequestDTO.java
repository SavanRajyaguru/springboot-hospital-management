package com.hostpital.hostpitalmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentRequestDTO {
    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Department name should only contain letters and spaces")
    private String name;

    private Long headDoctorId;
}