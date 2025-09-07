package com.hostpital.hostpitalmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceResponseDTO {
    private Long id;
    private String policyNumber;
    private String name;
    private String providerName;
    private LocalDate validUntil;
}
