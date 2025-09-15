package com.hostpital.hostpitalmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.hostpital.hostpitalmanagement.dto.DoctorRequestDTO;
import com.hostpital.hostpitalmanagement.dto.DoctorResponseDTO;
import com.hostpital.hostpitalmanagement.entity.Doctor;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DoctorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    Doctor toEntity(DoctorRequestDTO dto);

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    DoctorResponseDTO toDto(Doctor entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    void updateEntity(DoctorRequestDTO dto, @MappingTarget Doctor entity);
}
