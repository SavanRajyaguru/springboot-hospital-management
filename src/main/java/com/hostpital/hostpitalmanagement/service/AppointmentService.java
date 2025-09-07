package com.hostpital.hostpitalmanagement.service;

import org.springframework.stereotype.Service;

import com.hostpital.hostpitalmanagement.entity.Appointment;
import com.hostpital.hostpitalmanagement.entity.Doctor;
import com.hostpital.hostpitalmanagement.entity.Patient;
import com.hostpital.hostpitalmanagement.repository.AppointmentRepository;
import com.hostpital.hostpitalmanagement.repository.DoctorRepository;
import com.hostpital.hostpitalmanagement.repository.PatientRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
        private final AppointmentRepository appointmentRepository;
        private final PatientRepository patientRepository;
        private final DoctorRepository doctorRepository;

        @Transactional // Ensure atomicity of the operation
        public Appointment createAppointment(Appointment appointment, Long patientId, Long doctorId) {
                // Implementation for creating an appointment
                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
                Patient patient = patientRepository.findById(patientId)
                                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
                appointment.setPatient(patient); // Set the patient for the appointment
                appointment.setDoctor(doctor); // Set the doctor for the appointment
                patient.getAppointments().add(appointment); // Maintain bidirectional relationship
                return appointmentRepository.save(appointment); // Save and return the appointment
        }

        @Transactional
        public Appointment reassignAppointmentToDoctor(Long appointmentId, Long newDoctorId) {
                // Implementation for reassigning an appointment to a different doctor
                Appointment appointment = appointmentRepository.findById(appointmentId)
                                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
                Doctor newDoctor = doctorRepository.findById(newDoctorId)
                                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
                appointment.setDoctor(newDoctor); // Set the new doctor for the appointment
                newDoctor.getAppointments().add(appointment); // Maintain bidirectional relationship
                return appointment;
        }
}
