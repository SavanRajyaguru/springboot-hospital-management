package com.hostpital.hostpitalmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hostpital.hostpitalmanagement.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Long id);
}
