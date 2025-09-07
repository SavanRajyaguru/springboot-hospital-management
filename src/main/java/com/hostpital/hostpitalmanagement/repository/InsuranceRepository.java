package com.hostpital.hostpitalmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hostpital.hostpitalmanagement.entity.Insurance;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    boolean existsByPolicyNumber(String policyNumber);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
