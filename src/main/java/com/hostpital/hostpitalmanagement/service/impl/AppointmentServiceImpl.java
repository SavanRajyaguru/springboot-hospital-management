package com.hostpital.hostpitalmanagement.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostpital.hostpitalmanagement.dto.AppointmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.AppointmentResponseDTO;
import com.hostpital.hostpitalmanagement.dto.PaginatedResponse;
import com.hostpital.hostpitalmanagement.entity.Appointment;
import com.hostpital.hostpitalmanagement.entity.Doctor;
import com.hostpital.hostpitalmanagement.entity.Patient;
import com.hostpital.hostpitalmanagement.mapper.AppointmentMapper;
import com.hostpital.hostpitalmanagement.repository.AppointmentRepository;
import com.hostpital.hostpitalmanagement.repository.DoctorRepository;
import com.hostpital.hostpitalmanagement.repository.PatientRepository;
import com.hostpital.hostpitalmanagement.service.AppointmentService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO appointmentRequest) {
        // Validate patient exists
        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + appointmentRequest.getPatientId()));

        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + appointmentRequest.getDoctorId()));

        // Check if doctor is available at the requested time
        if (appointmentRepository.existsByDoctorAndAppointmentDate(doctor, appointmentRequest.getAppointmentDate())) {
            throw new IllegalStateException("Doctor is not available at the requested time: " + appointmentRequest.getAppointmentDate());
        }

        Appointment appointment = appointmentMapper.toEntity(appointmentRequest);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(appointmentMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AppointmentResponseDTO> getAllAppointments(int page, int limit, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);
        Page<Appointment> appointmentPage = appointmentRepository.findAll(pageRequest);

        return PaginatedResponse.<AppointmentResponseDTO>builder()
                .data(appointmentPage.getContent().stream()
                        .map(appointmentMapper::toDto)
                        .toList())
                .totalCount(appointmentPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(appointmentPage.getTotalPages())
                .build();
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO appointmentRequest) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));

        // Validate patient exists
        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + appointmentRequest.getPatientId()));

        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + appointmentRequest.getDoctorId()));

        // Check if doctor is available at the requested time (excluding current appointment)
        if (appointmentRepository.existsByDoctorAndAppointmentDateAndIdNot(doctor, appointmentRequest.getAppointmentDate(), id)) {
            throw new IllegalStateException("Doctor is not available at the requested time: " + appointmentRequest.getAppointmentDate());
        }

        // Update appointment details
        existingAppointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        existingAppointment.setReason(appointmentRequest.getReason());
        existingAppointment.setPatient(patient);
        existingAppointment.setDoctor(doctor);

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return appointmentMapper.toDto(updatedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId, int page, int limit) {
        // Verify patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new EntityNotFoundException("Patient not found with id: " + patientId);
        }

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "appointmentDate"));
        Page<Appointment> appointmentPage = appointmentRepository.findByPatient_Id(patientId, pageRequest);

        return PaginatedResponse.<AppointmentResponseDTO>builder()
                .data(appointmentPage.getContent().stream()
                        .map(appointmentMapper::toDto)
                        .toList())
                .totalCount(appointmentPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(appointmentPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId, int page, int limit) {
        // Verify doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new EntityNotFoundException("Doctor not found with id: " + doctorId);
        }

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "appointmentDate"));
        Page<Appointment> appointmentPage = appointmentRepository.findByDoctor_Id(doctorId, pageRequest);

        return PaginatedResponse.<AppointmentResponseDTO>builder()
                .data(appointmentPage.getContent().stream()
                        .map(appointmentMapper::toDto)
                        .toList())
                .totalCount(appointmentPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(appointmentPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AppointmentResponseDTO> getUpcomingAppointmentsByPatient(Long patientId, int page, int limit) {
        // Verify patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new EntityNotFoundException("Patient not found with id: " + patientId);
        }

        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Appointment> appointmentPage = appointmentRepository.findUpcomingAppointmentsByPatient(patientId, LocalDateTime.now(), pageRequest);

        return PaginatedResponse.<AppointmentResponseDTO>builder()
                .data(appointmentPage.getContent().stream()
                        .map(appointmentMapper::toDto)
                        .toList())
                .totalCount(appointmentPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(appointmentPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AppointmentResponseDTO> getUpcomingAppointmentsByDoctor(Long doctorId, int page, int limit) {
        // Verify doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new EntityNotFoundException("Doctor not found with id: " + doctorId);
        }

        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Appointment> appointmentPage = appointmentRepository.findUpcomingAppointmentsByDoctor(doctorId, LocalDateTime.now(), pageRequest);

        return PaginatedResponse.<AppointmentResponseDTO>builder()
                .data(appointmentPage.getContent().stream()
                        .map(appointmentMapper::toDto)
                        .toList())
                .totalCount(appointmentPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(appointmentPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AppointmentResponseDTO> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "appointmentDate"));
        Page<Appointment> appointmentPage = appointmentRepository.findByAppointmentDateBetween(startDate, endDate, pageRequest);

        return PaginatedResponse.<AppointmentResponseDTO>builder()
                .data(appointmentPage.getContent().stream()
                        .map(appointmentMapper::toDto)
                        .toList())
                .totalCount(appointmentPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(appointmentPage.getTotalPages())
                .build();
    }
}