package com.hostpital.hostpitalmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hostpital.hostpitalmanagement.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
