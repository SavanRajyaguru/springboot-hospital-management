package com.hostpital.hostpitalmanagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.hostpital.hostpitalmanagement.entity.type.BloodGroupType;
import com.hostpital.hostpitalmanagement.entity.type.GenderType;
import lombok.Data;

@Data
public class PatientResponseDTO {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String email;
    private GenderType gender;
    private BloodGroupType bloodGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long insuranceId;
}
