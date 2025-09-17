package com.hostpital.hostpitalmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hostpital.hostpitalmanagement.dto.DepartmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DepartmentResponseDTO;
import com.hostpital.hostpitalmanagement.entity.Department;
import com.hostpital.hostpitalmanagement.exception.DuplicateResourceException;
import com.hostpital.hostpitalmanagement.mapper.DepartmentMapper;
import com.hostpital.hostpitalmanagement.repository.DepartmentRepository;
import com.hostpital.hostpitalmanagement.repository.DoctorRepository;
import com.hostpital.hostpitalmanagement.service.impl.DepartmentServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private DepartmentRequestDTO departmentRequestDTO;
    private Department department;
    private DepartmentResponseDTO departmentResponseDTO;

    @BeforeEach
    void setUp() {
        departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("Cardiology");
        departmentRequestDTO.setHeadDoctorId(null);

        department = new Department();
        department.setId(1L);
        department.setName("Cardiology");
        department.setHeadDoctor(null);

        departmentResponseDTO = new DepartmentResponseDTO();
        departmentResponseDTO.setId(1L);
        departmentResponseDTO.setName("Cardiology");
        departmentResponseDTO.setHeadDoctorId(null);
        departmentResponseDTO.setHeadDoctorName(null);
        departmentResponseDTO.setDoctorCount(0);
    }

    @Test
    void testCreateDepartment_Success() {
        // Arrange
        when(departmentRepository.existsByName(anyString())).thenReturn(false);
        when(departmentMapper.toEntity(any(DepartmentRequestDTO.class))).thenReturn(department);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toDto(any(Department.class))).thenReturn(departmentResponseDTO);

        // Act
        DepartmentResponseDTO result = departmentService.createDepartment(departmentRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Cardiology", result.getName());
        assertEquals(1L, result.getId());
        verify(departmentRepository).existsByName("Cardiology");
        verify(departmentRepository).save(department);
        verify(departmentMapper).toEntity(departmentRequestDTO);
        verify(departmentMapper).toDto(department);
    }

    @Test
    void testCreateDepartment_DuplicateName() {
        // Arrange
        when(departmentRepository.existsByName(anyString())).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> departmentService.createDepartment(departmentRequestDTO));

        assertEquals("Department with name 'Cardiology' already exists", exception.getMessage());
        verify(departmentRepository).existsByName("Cardiology");
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testGetDepartmentById_Success() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentMapper.toDto(any(Department.class))).thenReturn(departmentResponseDTO);

        // Act
        DepartmentResponseDTO result = departmentService.getDepartmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Cardiology", result.getName());
        verify(departmentRepository).findById(1L);
        verify(departmentMapper).toDto(department);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> departmentService.getDepartmentById(1L));

        assertEquals("Department not found with id: 1", exception.getMessage());
        verify(departmentRepository).findById(1L);
        verify(departmentMapper, never()).toDto(any());
    }

    @Test
    void testDeleteDepartment_Success() {
        // Arrange
        department.setDoctors(new java.util.HashSet<>()); // Empty set of doctors
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        // Act
        assertDoesNotThrow(() -> departmentService.deleteDepartment(1L));

        // Assert
        verify(departmentRepository).findById(1L);
        verify(departmentRepository).deleteById(1L);
    }

    @Test
    void testDeleteDepartment_NotFound() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> departmentService.deleteDepartment(1L));

        assertEquals("Department not found with id: 1", exception.getMessage());
        verify(departmentRepository).findById(1L);
        verify(departmentRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteDepartment_WithAssignedDoctors() {
        // Arrange
        com.hostpital.hostpitalmanagement.entity.Doctor doctor1 = new com.hostpital.hostpitalmanagement.entity.Doctor();
        doctor1.setId(1L);
        doctor1.setName("Dr. Smith");
        
        java.util.Set<com.hostpital.hostpitalmanagement.entity.Doctor> doctors = new java.util.HashSet<>();
        doctors.add(doctor1);
        department.setDoctors(doctors);
        
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> departmentService.deleteDepartment(1L));

        assertEquals("Cannot delete department with assigned doctors. Please reassign doctors first.", exception.getMessage());
        verify(departmentRepository).findById(1L);
        verify(departmentRepository, never()).deleteById(any());
    }
}
