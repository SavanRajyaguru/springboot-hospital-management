package com.hostpital.hostpitalmanagement.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;
    
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
}