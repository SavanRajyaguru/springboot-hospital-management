package com.hostpital.hostpitalmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hostpital.hostpitalmanagement.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Doctor> findByDepartment_Id(Long departmentId, Pageable pageable);

    Page<Doctor> findBySpecialization(String specialization, Pageable pageable);
}
