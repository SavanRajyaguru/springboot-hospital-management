package com.hostpital.hostpitalmanagement.service;

import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.dto.PatientRequestDTO;
import com.hostpital.hostpitalmanagement.dto.PatientResponseDTO;

public interface PatientService {
    // Define service methods for Patient entity
    PatientResponseDTO createPatient(PatientRequestDTO patientRequest);

    PatientResponseDTO getPatientById(Long id);

    void deletePatient(Long id);

    PaginatedResponse<PatientResponseDTO> getAllPatients(int page, int limit, String sortBy, String sortDirection);

    PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequest);
}
