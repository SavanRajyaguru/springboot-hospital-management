package com.hostpital.hostpitalmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hostpital.hostpitalmanagement.dto.ApiResponse;
import com.hostpital.hostpitalmanagement.dto.DepartmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DepartmentResponseDTO;
import com.hostpital.hostpitalmanagement.dto.DoctorResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(
            @Valid @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO response = departmentService.createDepartment(request);
        return new ResponseEntity<>(ApiResponse.success(response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartment(@PathVariable Long id) {
        DepartmentResponseDTO response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<DepartmentResponseDTO>>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResponse<DepartmentResponseDTO> response = departmentService.getAllDepartments(page, limit, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{id}/doctors")
    public ResponseEntity<ApiResponse<PaginatedResponse<DoctorResponseDTO>>> getDoctorsByDepartment(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<DoctorResponseDTO> response = departmentService.getDoctorsByDepartment(id, page, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}