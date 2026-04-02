package com.jobtracker.backend.dto;

import lombok.Data;

@Data
public class AiAnalysisResponse {
    private Integer matchScore;
    private String feedback;
    private String missingKeywords;
}
