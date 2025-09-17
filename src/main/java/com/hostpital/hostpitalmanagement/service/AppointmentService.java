package com.hostpital.hostpitalmanagement.service;

import java.time.LocalDateTime;

import com.hostpital.hostpitalmanagement.dto.AppointmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.AppointmentResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO appointmentRequest);
    
    AppointmentResponseDTO getAppointmentById(Long id);
    
    PaginatedResponse<AppointmentResponseDTO> getAllAppointments(int page, int limit, String sortBy, String sortDirection);
    
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO appointmentRequest);
    
    void deleteAppointment(Long id);
    
    PaginatedResponse<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId, int page, int limit);
    
    PaginatedResponse<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId, int page, int limit);
    
    PaginatedResponse<AppointmentResponseDTO> getUpcomingAppointmentsByPatient(Long patientId, int page, int limit);
    
    PaginatedResponse<AppointmentResponseDTO> getUpcomingAppointmentsByDoctor(Long doctorId, int page, int limit);
    
    PaginatedResponse<AppointmentResponseDTO> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int limit);
}
