package com.hostpital.hostpitalmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hostpital.hostpitalmanagement.dto.ApiResponse;
import com.hostpital.hostpitalmanagement.dto.InsuranceRequestDTO;
import com.hostpital.hostpitalmanagement.dto.InsuranceResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.service.InsuranceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/insurances")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @PostMapping
    public ResponseEntity<ApiResponse<InsuranceResponseDTO>> createInsurance(
            @Valid @RequestBody InsuranceRequestDTO request) {
        InsuranceResponseDTO response = insuranceService.createInsurance(request);
        return new ResponseEntity<>(ApiResponse.success(response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InsuranceResponseDTO>> getInsurance(@PathVariable Long id) {
        InsuranceResponseDTO response = insuranceService.getInsuranceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<InsuranceResponseDTO>>> getAllInsurances(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        PaginatedResponse<InsuranceResponseDTO> response = insuranceService.getAllInsurances(page, limit, sortBy,
                sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InsuranceResponseDTO>> updateInsurance(
            @PathVariable Long id,
            @Valid @RequestBody InsuranceRequestDTO request) {
        InsuranceResponseDTO response = insuranceService.updateInsurance(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInsurance(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
