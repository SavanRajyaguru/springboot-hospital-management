package com.hostpital.hostpitalmanagement.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class ValidationErrorResponse {
    private final Map<String, String> fieldErrors = new HashMap<>();
    private final List<String> globalErrors = new ArrayList<>();

    public void addFieldError(String field, String message) {
        fieldErrors.put(field, message);
    }

    public void addGlobalError(String message) {
        globalErrors.add(message);
    }
}
