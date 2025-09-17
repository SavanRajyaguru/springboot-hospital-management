package com.hostpital.hostpitalmanagement.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostpital.hostpitalmanagement.dto.DoctorRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DoctorResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.entity.Department;
import com.hostpital.hostpitalmanagement.entity.Doctor;
import com.hostpital.hostpitalmanagement.exception.DuplicateResourceException;
import jakarta.persistence.EntityNotFoundException;
import com.hostpital.hostpitalmanagement.mapper.DoctorMapper;
import com.hostpital.hostpitalmanagement.repository.DepartmentRepository;
import com.hostpital.hostpitalmanagement.repository.DoctorRepository;
import com.hostpital.hostpitalmanagement.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO request) {
        // Check if email is already in use
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Doctor with email " + request.getEmail() + " already exists");
        }

        // Verify department exists
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Department not found with id: " + request.getDepartmentId()));

        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setDepartment(department);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(doctorMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<DoctorResponseDTO> getAllDoctors(int page, int limit, String sortBy,
            String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        Page<Doctor> doctorPage = doctorRepository.findAll(pageRequest);

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

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO request) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));

        // Check if email is already in use by another doctor
        if (doctorRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException("Doctor with email " + request.getEmail() + " already exists");
        }

        // Verify department exists
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Department not found with id: " + request.getDepartmentId()));

        doctorMapper.updateEntity(request, existingDoctor);
        existingDoctor.setDepartment(department);
        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return doctorMapper.toDto(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
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

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<DoctorResponseDTO> getDoctorsBySpecialization(String specialization, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Doctor> doctorPage = doctorRepository.findBySpecialization(specialization, pageRequest);

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
