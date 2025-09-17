package com.hostpital.hostpitalmanagement.service;

import com.hostpital.hostpitalmanagement.dto.DepartmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DepartmentResponseDTO;
import com.hostpital.hostpitalmanagement.dto.DoctorResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;

public interface DepartmentService {
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequest);
    
    DepartmentResponseDTO getDepartmentById(Long id);
    
    PaginatedResponse<DepartmentResponseDTO> getAllDepartments(int page, int limit, String sortBy, String sortDirection);
    
    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO departmentRequest);
    
    void deleteDepartment(Long id);
    
    PaginatedResponse<DoctorResponseDTO> getDoctorsByDepartment(Long departmentId, int page, int limit);
}