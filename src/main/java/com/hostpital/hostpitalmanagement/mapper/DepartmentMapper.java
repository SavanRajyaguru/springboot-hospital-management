package com.hostpital.hostpitalmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.hostpital.hostpitalmanagement.dto.DepartmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DepartmentResponseDTO;
import com.hostpital.hostpitalmanagement.entity.Department;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "headDoctor", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    Department toEntity(DepartmentRequestDTO dto);

    @Mapping(target = "headDoctorId", source = "headDoctor.id")
    @Mapping(target = "headDoctorName", source = "headDoctor.name")
    @Mapping(target = "doctorCount", expression = "java(entity.getDoctors() != null ? entity.getDoctors().size() : 0)")
    DepartmentResponseDTO toDto(Department entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "headDoctor", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    void updateEntity(DepartmentRequestDTO dto, @MappingTarget Department entity);
}