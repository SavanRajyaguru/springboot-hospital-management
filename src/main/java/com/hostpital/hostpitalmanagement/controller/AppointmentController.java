package com.hostpital.hostpitalmanagement.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hostpital.hostpitalmanagement.dto.ApiResponse;
import com.hostpital.hostpitalmanagement.dto.AppointmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.AppointmentResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management", description = "APIs for managing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Create a new appointment", description = "Creates a new appointment with the provided information")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO request) {
        AppointmentResponseDTO response = appointmentService.createAppointment(request);
        return new ResponseEntity<>(ApiResponse.success(response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieves an appointment's information by its ID")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getAppointment(@PathVariable Long id) {
        AppointmentResponseDTO response = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all appointments", description = "Retrieves all appointments with pagination and sorting options")
    public ResponseEntity<ApiResponse<PaginatedResponse<AppointmentResponseDTO>>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResponse<AppointmentResponseDTO> response = appointmentService.getAllAppointments(page, limit, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update appointment", description = "Updates an appointment's information by its ID")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO request) {
        AppointmentResponseDTO response = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete appointment", description = "Deletes an appointment by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get appointments by patient", description = "Retrieves all appointments for a specific patient")
    public ResponseEntity<ApiResponse<PaginatedResponse<AppointmentResponseDTO>>> getAppointmentsByPatient(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<AppointmentResponseDTO> response = appointmentService.getAppointmentsByPatient(patientId, page, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get appointments by doctor", description = "Retrieves all appointments for a specific doctor")
    public ResponseEntity<ApiResponse<PaginatedResponse<AppointmentResponseDTO>>> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<AppointmentResponseDTO> response = appointmentService.getAppointmentsByDoctor(doctorId, page, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/patient/{patientId}/upcoming")
    @Operation(summary = "Get upcoming appointments by patient", description = "Retrieves upcoming appointments for a specific patient")
    public ResponseEntity<ApiResponse<PaginatedResponse<AppointmentResponseDTO>>> getUpcomingAppointmentsByPatient(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<AppointmentResponseDTO> response = appointmentService.getUpcomingAppointmentsByPatient(patientId, page, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/doctor/{doctorId}/upcoming")
    @Operation(summary = "Get upcoming appointments by doctor", description = "Retrieves upcoming appointments for a specific doctor")
    public ResponseEntity<ApiResponse<PaginatedResponse<AppointmentResponseDTO>>> getUpcomingAppointmentsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<AppointmentResponseDTO> response = appointmentService.getUpcomingAppointmentsByDoctor(doctorId, page, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get appointments by date range", description = "Retrieves appointments within a specific date range")
    public ResponseEntity<ApiResponse<PaginatedResponse<AppointmentResponseDTO>>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<AppointmentResponseDTO> response = appointmentService.getAppointmentsByDateRange(startDate, endDate, page, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}