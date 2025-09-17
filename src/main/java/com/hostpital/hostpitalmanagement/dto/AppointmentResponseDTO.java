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
public class AppointmentResponseDTO {
    private Long id;
    private LocalDateTime appointmentDate;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Patient information
    private Long patientId;
    private String patientName;
    private String patientEmail;
    
    // Doctor information
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private String departmentName;
}