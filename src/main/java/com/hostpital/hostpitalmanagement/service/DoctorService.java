package com.hostpital.hostpitalmanagement.service;

import com.hostpital.hostpitalmanagement.dto.DoctorRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DoctorResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;

public interface DoctorService {
    DoctorResponseDTO createDoctor(DoctorRequestDTO request);

    DoctorResponseDTO getDoctorById(Long id);

    PaginatedResponse<DoctorResponseDTO> getAllDoctors(int page, int limit, String sortBy, String sortDirection);

    DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO request);

    void deleteDoctor(Long id);

    PaginatedResponse<DoctorResponseDTO> getDoctorsByDepartment(Long departmentId, int page, int limit);

    PaginatedResponse<DoctorResponseDTO> getDoctorsBySpecialization(String specialization, int page, int limit);
}
