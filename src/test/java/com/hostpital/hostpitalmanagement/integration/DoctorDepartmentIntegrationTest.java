package com.hostpital.hostpitalmanagement.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hostpital.hostpitalmanagement.dto.DoctorRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DoctorResponseDTO;
import com.hostpital.hostpitalmanagement.entity.Department;
import com.hostpital.hostpitalmanagement.entity.Doctor;
import com.hostpital.hostpitalmanagement.mapper.DoctorMapper;
import com.hostpital.hostpitalmanagement.repository.DepartmentRepository;
import com.hostpital.hostpitalmanagement.repository.DoctorRepository;
import com.hostpital.hostpitalmanagement.service.impl.DoctorServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DoctorDepartmentIntegrationTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private DoctorRequestDTO doctorRequestDTO;
    private Doctor doctor;
    private Department department;
    private DoctorResponseDTO doctorResponseDTO;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Cardiology");

        doctorRequestDTO = DoctorRequestDTO.builder()
                .name("Dr. John Smith")
                .email("dr.john@hospital.com")
                .phoneNumber("+1234567890")
                .specialization("Cardiology")
                .departmentId(1L)
                .qualifications("MD, MBBS")
                .experience("5 years in cardiology")
                .availableDays("MON,TUE,WED")
                .availableHours("09:00-17:00")
                .build();

        doctor = Doctor.builder()
                .id(1L)
                .name("Dr. John Smith")
                .email("dr.john@hospital.com")
                .contactNumber("+1234567890")
                .specialization("Cardiology")
                .qualifications("MD, MBBS")
                .experience("5 years in cardiology")
                .availableDays("MON,TUE,WED")
                .availableHours("09:00-17:00")
                .department(department)
                .build();

        doctorResponseDTO = DoctorResponseDTO.builder()
                .id(1L)
                .name("Dr. John Smith")
                .email("dr.john@hospital.com")
                .phoneNumber("+1234567890")
                .specialization("Cardiology")
                .departmentId(1L)
                .departmentName("Cardiology")
                .qualifications("MD, MBBS")
                .experience("5 years in cardiology")
                .availableDays("MON,TUE,WED")
                .availableHours("09:00-17:00")
                .build();
    }

    @Test
    void testCreateDoctor_WithValidDepartment_Success() {
        // Arrange
        when(doctorRepository.existsByEmail(doctorRequestDTO.getEmail())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(doctorMapper.toEntity(doctorRequestDTO)).thenReturn(doctor);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorResponseDTO);

        // Act
        DoctorResponseDTO result = doctorService.createDoctor(doctorRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getDepartmentId());
        assertEquals("Cardiology", result.getDepartmentName());
        assertEquals("Dr. John Smith", result.getName());
        
        verify(departmentRepository).findById(1L);
        verify(doctorRepository).save(doctor);
        // Verify that the doctor was assigned to the department
        assertEquals(department, doctor.getDepartment());
    }

    @Test
    void testCreateDoctor_WithInvalidDepartment_ThrowsException() {
        // Arrange
        when(doctorRepository.existsByEmail(doctorRequestDTO.getEmail())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> doctorService.createDoctor(doctorRequestDTO));

        assertEquals("Department not found with id: 1", exception.getMessage());
        verify(departmentRepository).findById(1L);
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void testUpdateDoctor_ChangeDepartment_Success() {
        // Arrange
        Department newDepartment = new Department();
        newDepartment.setId(2L);
        newDepartment.setName("Neurology");

        DoctorRequestDTO updateRequest = DoctorRequestDTO.builder()
                .name("Dr. John Smith")
                .email("dr.john@hospital.com")
                .phoneNumber("+1234567890")
                .specialization("Neurology")
                .departmentId(2L)
                .qualifications("MD, MBBS, Neurology Specialist")
                .experience("7 years total, 2 years in neurology")
                .availableDays("MON,WED,FRI")
                .availableHours("10:00-18:00")
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByEmailAndIdNot(updateRequest.getEmail(), 1L)).thenReturn(false);
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(newDepartment));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorResponseDTO);

        // Act
        DoctorResponseDTO result = doctorService.updateDoctor(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(departmentRepository).findById(2L);
        verify(doctorMapper).updateEntity(updateRequest, doctor);
        // Verify that the doctor's department was updated
        assertEquals(newDepartment, doctor.getDepartment());
    }

    @Test
    void testUpdateDoctor_WithInvalidDepartment_ThrowsException() {
        // Arrange
        DoctorRequestDTO updateRequest = DoctorRequestDTO.builder()
                .name("Dr. John Smith")
                .email("dr.john@hospital.com")
                .phoneNumber("+1234567890")
                .specialization("Neurology")
                .departmentId(999L) // Invalid department ID
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByEmailAndIdNot(updateRequest.getEmail(), 1L)).thenReturn(false);
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> doctorService.updateDoctor(1L, updateRequest));

        assertEquals("Department not found with id: 999", exception.getMessage());
        verify(departmentRepository).findById(999L);
        verify(doctorRepository, never()).save(any());
    }
}