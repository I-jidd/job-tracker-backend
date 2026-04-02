package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.AiAnalysisRequest;
import com.jobtracker.backend.dto.AiAnalysisResponse;
import com.jobtracker.backend.model.Application;
import com.jobtracker.backend.model.User;
import com.jobtracker.backend.repository.ApplicationRepository;
import com.jobtracker.backend.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final ApplicationRepository applicationRepository;

    @PostMapping("/analyze-match")
    public ResponseEntity<AiAnalysisResponse> analyzeMatch(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AiAnalysisRequest request) {

        AiAnalysisResponse response = aiService.analyzeResume(
                request.getResumeText(),
                request.getJobDescription()
        );

        // Save results to application if applicationId provided
        if (request.getApplicationId() != null) {
            try {
                UUID id = UUID.fromString(request.getApplicationId());
                applicationRepository.findById(id).ifPresent(app -> {
                    if (app.getUser().getId().equals(user.getId())) {
                        app.setMatchScore(response.getMatchScore());
                        app.setAiFeedback(response.getFeedback());
                        applicationRepository.save(app);
                    }
                });
            } catch (Exception e) {
                // Invalid UUID — skip saving
            }
        }

        return ResponseEntity.ok(response);
    }
}