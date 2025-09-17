package com.hostpital.hostpitalmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hostpital.hostpitalmanagement.entity.Appointment;
import com.hostpital.hostpitalmanagement.entity.Doctor;
import com.hostpital.hostpitalmanagement.entity.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
    
    Page<Appointment> findByPatient_Id(Long patientId, Pageable pageable);

    List<Appointment> findByDoctor(Doctor doctor);
    
    Page<Appointment> findByDoctor_Id(Long doctorId, Pageable pageable);

    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);
    
    Page<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Check if doctor is available at specific time
    boolean existsByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentDate);
    
    // Check if doctor is available at specific time (excluding current appointment for updates)
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDate = :appointmentDate AND a.id <> :appointmentId")
    boolean existsByDoctorAndAppointmentDateAndIdNot(@Param("doctor") Doctor doctor, @Param("appointmentDate") LocalDateTime appointmentDate, @Param("appointmentId") Long appointmentId);
    
    // Get upcoming appointments for a patient
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.appointmentDate > :currentDate ORDER BY a.appointmentDate")
    Page<Appointment> findUpcomingAppointmentsByPatient(@Param("patientId") Long patientId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);
    
    // Get upcoming appointments for a doctor
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate > :currentDate ORDER BY a.appointmentDate")
    Page<Appointment> findUpcomingAppointmentsByDoctor(@Param("doctorId") Long doctorId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);
}
