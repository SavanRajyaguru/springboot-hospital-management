package com.hostpital.hostpitalmanagement.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String specialization;
    private Long departmentId;
    private String departmentName;
    private String qualifications;
    private String experience;
    private String availableDays;
    private String availableHours;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
