package com.hostpital.hostpitalmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.hostpital.hostpitalmanagement.dto.InsuranceRequestDTO;
import com.hostpital.hostpitalmanagement.dto.InsuranceResponseDTO;
import com.hostpital.hostpitalmanagement.entity.Insurance;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InsuranceMapper {
    @org.mapstruct.Mapping(source = "providerName", target = "provider")
    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    @org.mapstruct.Mapping(target = "policyNumber", ignore = true)
    @org.mapstruct.Mapping(target = "patient", ignore = true)

    Insurance toEntity(InsuranceRequestDTO dto);

    @org.mapstruct.Mapping(source = "provider", target = "providerName")

    InsuranceResponseDTO toDto(Insurance entity);

    @org.mapstruct.Mapping(source = "providerName", target = "provider")
    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    @org.mapstruct.Mapping(target = "policyNumber", ignore = true)
    @org.mapstruct.Mapping(target = "patient", ignore = true)

    void updateEntity(InsuranceRequestDTO dto, @MappingTarget Insurance entity);
}
