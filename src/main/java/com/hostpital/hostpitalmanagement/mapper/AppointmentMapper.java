package com.hostpital.hostpitalmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hostpital.hostpitalmanagement.dto.AppointmentRequestDTO;
import com.hostpital.hostpitalmanagement.dto.AppointmentResponseDTO;
import com.hostpital.hostpitalmanagement.entity.Appointment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    Appointment toEntity(AppointmentRequestDTO dto);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "patientEmail", source = "patient.email")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.name")
    @Mapping(target = "doctorSpecialization", source = "doctor.specialization")
    @Mapping(target = "departmentName", source = "doctor.department.name")
    AppointmentResponseDTO toDto(Appointment entity);
}