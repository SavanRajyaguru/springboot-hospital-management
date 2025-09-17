package com.hostpital.hostpitalmanagement.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostpital.hostpitalmanagement.dto.DepartmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DepartmentResponseDTO;
import com.hostpital.hostpitalmanagement.dto.DoctorResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.entity.Department;
import com.hostpital.hostpitalmanagement.entity.Doctor;
import com.hostpital.hostpitalmanagement.exception.DuplicateResourceException;
import com.hostpital.hostpitalmanagement.mapper.DepartmentMapper;
import com.hostpital.hostpitalmanagement.mapper.DoctorMapper;
import com.hostpital.hostpitalmanagement.repository.DepartmentRepository;
import com.hostpital.hostpitalmanagement.repository.DoctorRepository;
import com.hostpital.hostpitalmanagement.service.DepartmentService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentMapper departmentMapper;
    private final DoctorMapper doctorMapper;

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequest) {
        // Check if department name already exists
        if (departmentRepository.existsByName(departmentRequest.getName())) {
            throw new DuplicateResourceException("Department with name '" + departmentRequest.getName() + "' already exists");
        }

        Department department = departmentMapper.toEntity(departmentRequest);
        
        // Set head doctor if provided
        if (departmentRequest.getHeadDoctorId() != null) {
            Doctor headDoctor = doctorRepository.findById(departmentRequest.getHeadDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + departmentRequest.getHeadDoctorId()));
            department.setHeadDoctor(headDoctor);
        }

        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<DepartmentResponseDTO> getAllDepartments(int page, int limit, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);
        Page<Department> departmentPage = departmentRepository.findAll(pageRequest);

        return PaginatedResponse.<DepartmentResponseDTO>builder()
                .data(departmentPage.getContent().stream()
                        .map(departmentMapper::toDto)
                        .toList())
                .totalCount(departmentPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(departmentPage.getTotalPages())
                .build();
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO departmentRequest) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        // Check if name is already in use by another department
        if (departmentRepository.existsByNameAndIdNot(departmentRequest.getName(), id)) {
            throw new DuplicateResourceException("Department with name '" + departmentRequest.getName() + "' already exists");
        }

        // Update department name
        existingDepartment.setName(departmentRequest.getName());
        
        // Update head doctor if provided
        if (departmentRequest.getHeadDoctorId() != null) {
            Doctor headDoctor = doctorRepository.findById(departmentRequest.getHeadDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + departmentRequest.getHeadDoctorId()));
            existingDepartment.setHeadDoctor(headDoctor);
        } else {
            existingDepartment.setHeadDoctor(null);
        }

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return departmentMapper.toDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
        
        // Check if department has doctors
        if (department.getDoctors() != null && !department.getDoctors().isEmpty()) {
            throw new IllegalStateException("Cannot delete department with assigned doctors. Please reassign doctors first.");
        }
        
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<DoctorResponseDTO> getDoctorsByDepartment(Long departmentId, int page, int limit) {
        // Verify department exists
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException("Department not found with id: " + departmentId);
        }

        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Doctor> doctorPage = doctorRepository.findByDepartment_Id(departmentId, pageRequest);

        return PaginatedResponse.<DoctorResponseDTO>builder()
                .data(doctorPage.getContent().stream()
                        .map(doctorMapper::toDto)
                        .toList())
                .totalCount(doctorPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(doctorPage.getTotalPages())
                .build();
    }
}