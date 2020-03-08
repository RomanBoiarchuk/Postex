package com.project.postex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.AbstractPropertyBindingResult;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ValidationErrorFormatter {

    public String format(AbstractPropertyBindingResult errors) {
        var errorMessages = errors
                .getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return String.join("\n", errorMessages);
    }
}
