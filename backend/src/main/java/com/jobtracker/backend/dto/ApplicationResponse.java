package com.jobtracker.backend.dto;

import com.jobtracker.backend.model.Application;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ApplicationResponse {

    private UUID id;
    private String jobTitle;
    private String company;
    private String jobUrl;
    private Application.Status status;
    private LocalDate appliedDate;
    private Integer matchScore;
    private String aiFeedback;

    public static ApplicationResponse fromEntity(Application app){
        ApplicationResponse response = new ApplicationResponse();
        response.setId(app.getId());
        response.setJobTitle(app.getJobTitle());
        response.setCompany(app.getCompany());
        response.setJobUrl(app.getJobUrl());
        response.setStatus(app.getStatus());
        response.setAppliedDate(app.getAppliedDate());
        response.setMatchScore(app.getMatchScore());
        response.setAiFeedback(app.getAiFeedback());
        return response;
    }
}
