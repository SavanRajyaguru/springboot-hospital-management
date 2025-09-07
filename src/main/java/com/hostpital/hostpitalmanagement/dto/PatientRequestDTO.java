package com.hostpital.hostpitalmanagement.dto;

import java.time.LocalDate;
import com.hostpital.hostpitalmanagement.entity.type.BloodGroupType;
import com.hostpital.hostpitalmanagement.entity.type.GenderType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientRequestDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name should only contain letters and spaces")
    private String name;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Gender is required")
    private GenderType gender;

    private BloodGroupType bloodGroup;
}
