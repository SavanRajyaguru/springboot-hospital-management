package com.hostpital.hostpitalmanagement.service;

import com.hostpital.hostpitalmanagement.dto.InsuranceRequestDTO;
import com.hostpital.hostpitalmanagement.dto.InsuranceResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;

public interface InsuranceService {
    // Define service methods related to insurance management
    InsuranceResponseDTO createInsurance(InsuranceRequestDTO request);

    InsuranceResponseDTO getInsuranceById(Long id);

    PaginatedResponse<InsuranceResponseDTO> getAllInsurances(int page, int limit, String sortBy, String sortDirection);

    InsuranceResponseDTO updateInsurance(Long id, InsuranceRequestDTO request);

    void deleteInsurance(Long id);

}
