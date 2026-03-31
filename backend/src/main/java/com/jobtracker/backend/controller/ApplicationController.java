package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.ApplicationRequest;
import com.jobtracker.backend.dto.ApplicationResponse;
import com.jobtracker.backend.model.Application;
import com.jobtracker.backend.model.User;
import com.jobtracker.backend.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // GET all applications
    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(applicationService.getAllApplications(user));
    }

    // POST create new application
    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.createApplication(user, request));
    }

    // PUT update status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @RequestParam Application.Status status) {
        return ResponseEntity.ok(applicationService.updateStatus(user, id, status));
    }

    // DELETE application
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id) {
        applicationService.deleteApplication(user, id);
        return ResponseEntity.noContent().build();
    }
}