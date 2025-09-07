package com.hostpital.hostpitalmanagement.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.hostpital.hostpitalmanagement.dto.PatientRequestDTO;
import com.hostpital.hostpitalmanagement.dto.PatientResponseDTO;
import com.hostpital.hostpitalmanagement.entity.Patient;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = { LocalDateTime.class })
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "insurance", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    Patient toEntity(PatientRequestDTO dto);

    @Mapping(target = "insuranceId", source = "insurance.id")
    PatientResponseDTO toDto(Patient entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "insurance", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    void updateEntity(PatientRequestDTO dto, @MappingTarget Patient entity);
}
