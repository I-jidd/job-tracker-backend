package com.jobtracker.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationRequest {

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Company is Required")
    private String company;

    private String jobUrl;

    private LocalDate appliedDate;
}
