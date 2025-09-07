package com.hostpital.hostpitalmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hostpital.hostpitalmanagement.dto.ApiResponse;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.dto.PatientRequestDTO;
import com.hostpital.hostpitalmanagement.dto.PatientResponseDTO;
import com.hostpital.hostpitalmanagement.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponseDTO>> createPatient(
            @Valid @RequestBody PatientRequestDTO request) {
        PatientResponseDTO response = patientService.createPatient(request);
        return new ResponseEntity<>(ApiResponse.success(response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getPatient(@PathVariable Long id) {
        PatientResponseDTO response = patientService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<PatientResponseDTO>>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResponse<PatientResponseDTO> response = patientService.getAllPatients(page, limit, sortBy,
                sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDTO request) {
        PatientResponseDTO response = patientService.updatePatient(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
