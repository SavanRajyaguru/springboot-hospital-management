package com.hostpital.hostpitalmanagement.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.dto.PatientRequestDTO;
import com.hostpital.hostpitalmanagement.dto.PatientResponseDTO;
import com.hostpital.hostpitalmanagement.entity.Patient;
import com.hostpital.hostpitalmanagement.mapper.PatientMapper;
import com.hostpital.hostpitalmanagement.repository.PatientRepository;
import com.hostpital.hostpitalmanagement.service.PatientService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.hostpital.hostpitalmanagement.exception.DuplicateResourceException;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequest) {
        // Check if email is already in use
        if (patientRepository.existsByEmail(patientRequest.getEmail())) {
            throw new DuplicateResourceException("Patient with email " + patientRequest.getEmail() + " already exists");
        }

        // Check if name is already in use
        if (patientRepository.existsByName(patientRequest.getName())) {
            throw new DuplicateResourceException("Patient with name " + patientRequest.getName() + " already exists");
        }

        Patient patient = patientMapper.toEntity(patientRequest);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<PatientResponseDTO> getAllPatients(int page, int limit, String sortBy,
            String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);
        Page<Patient> patientPage = patientRepository.findAll(pageRequest);

        return PaginatedResponse.<PatientResponseDTO>builder()
                .data(patientPage.getContent().stream()
                        .map(patientMapper::toDto)
                        .toList())
                .totalCount(patientPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(patientPage.getTotalPages())
                .build();
    }

    @Override
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequest) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        // Check if email is already in use by another patient
        if (patientRepository.existsByEmailAndIdNot(patientRequest.getEmail(), id)) {
            throw new DuplicateResourceException("Patient with email " + patientRequest.getEmail() + " already exists");
        }

        // Check if name is already in use by another patient
        if (patientRepository.existsByNameAndIdNot(patientRequest.getName(), id)) {
            throw new DuplicateResourceException("Patient with name " + patientRequest.getName() + " already exists");
        }

        patientMapper.updateEntity(patientRequest, existingPatient);
        Patient updatedPatient = patientRepository.save(existingPatient);
        return patientMapper.toDto(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}
