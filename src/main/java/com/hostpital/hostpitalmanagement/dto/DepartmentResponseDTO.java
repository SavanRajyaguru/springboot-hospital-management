package com.hostpital.hostpitalmanagement.dto;

import lombok.Data;

@Data
public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private Long headDoctorId;
    private String headDoctorName;
    private int doctorCount;
}