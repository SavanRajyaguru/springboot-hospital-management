package com.hostpital.hostpitalmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hostpital.hostpitalmanagement.dto.ApiResponse;
import com.hostpital.hostpitalmanagement.dto.DoctorRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DoctorResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.service.DoctorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Management", description = "APIs for managing doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "Create a new doctor", description = "Creates a new doctor with the provided information")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> createDoctor(
            @Valid @RequestBody DoctorRequestDTO request) {
        DoctorResponseDTO response = doctorService.createDoctor(request);
        return new ResponseEntity<>(ApiResponse.success(response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieves a doctor's information by their ID")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> getDoctor(@PathVariable Long id) {
        DoctorResponseDTO response = doctorService.getDoctorById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieves all doctors with pagination and sorting options")
    public ResponseEntity<ApiResponse<PaginatedResponse<DoctorResponseDTO>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        PaginatedResponse<DoctorResponseDTO> response = doctorService.getAllDoctors(page, limit, sortBy,
                sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update doctor", description = "Updates a doctor's information by their ID")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDTO request) {
        DoctorResponseDTO response = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor", description = "Deletes a doctor by their ID")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get doctors by department", description = "Retrieves all doctors in a specific department")
    public ResponseEntity<ApiResponse<PaginatedResponse<DoctorResponseDTO>>> getDoctorsByDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<DoctorResponseDTO> response = doctorService.getDoctorsByDepartment(departmentId, page, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Get doctors by specialization", description = "Retrieves all doctors with a specific specialization")
    public ResponseEntity<ApiResponse<PaginatedResponse<DoctorResponseDTO>>> getDoctorsBySpecialization(
            @PathVariable String specialization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<DoctorResponseDTO> response = doctorService.getDoctorsBySpecialization(specialization, page,
                limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
