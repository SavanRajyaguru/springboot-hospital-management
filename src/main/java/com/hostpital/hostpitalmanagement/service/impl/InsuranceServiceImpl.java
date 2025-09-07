package com.hostpital.hostpitalmanagement.service.impl;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostpital.hostpitalmanagement.dto.InsuranceRequestDTO;
import com.hostpital.hostpitalmanagement.dto.InsuranceResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.entity.Insurance;
import com.hostpital.hostpitalmanagement.mapper.InsuranceMapper;
import com.hostpital.hostpitalmanagement.repository.InsuranceRepository;
import com.hostpital.hostpitalmanagement.service.InsuranceService;

import jakarta.persistence.EntityNotFoundException;
import com.hostpital.hostpitalmanagement.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final InsuranceMapper insuranceMapper;

    private String generatePolicyNumber() {
        LocalDateTime now = LocalDateTime.now();
        String yearPrefix = String.valueOf(now.getYear()).substring(2); // Get last 2 digits of year
        String serialNumber = String.format("%06d", insuranceRepository.count() + 1); // 6-digit sequential number
        return "POL" + yearPrefix + serialNumber;
    }

    @Override
    public InsuranceResponseDTO createInsurance(InsuranceRequestDTO request) {
        if (insuranceRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Insurance with name '" + request.getName() + "' already exists");
        }
        Insurance insurance = insuranceMapper.toEntity(request);
        insurance.setPolicyNumber(generatePolicyNumber());
        Insurance savedInsurance = insuranceRepository.save(insurance);
        return insuranceMapper.toDto(savedInsurance);
    }

    @Override
    @Transactional(readOnly = true)
    public InsuranceResponseDTO getInsuranceById(Long id) {
        return insuranceRepository.findById(id)
                .map(insuranceMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<InsuranceResponseDTO> getAllInsurances(int page, int limit, String sortBy,
            String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        Page<Insurance> insurancePage = insuranceRepository.findAll(pageRequest);

        return PaginatedResponse.<InsuranceResponseDTO>builder()
                .data(insurancePage.getContent().stream()
                        .map(insuranceMapper::toDto)
                        .toList())
                .totalCount(insurancePage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(insurancePage.getTotalPages())
                .build();
    }

    @Override
    public InsuranceResponseDTO updateInsurance(Long id, InsuranceRequestDTO request) {
        Insurance existingInsurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));

        if (insuranceRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new DuplicateResourceException("Insurance with name '" + request.getName() + "' already exists");
        }

        insuranceMapper.updateEntity(request, existingInsurance);
        Insurance updatedInsurance = insuranceRepository.save(existingInsurance);
        return insuranceMapper.toDto(updatedInsurance);
    }

    @Override
    public void deleteInsurance(Long id) {
        if (!insuranceRepository.existsById(id)) {
            throw new EntityNotFoundException("Insurance not found with id: " + id);
        }
        insuranceRepository.deleteById(id);
    }
}
