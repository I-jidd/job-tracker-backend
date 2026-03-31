package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.ApplicationRequest;
import com.jobtracker.backend.dto.ApplicationResponse;
import com.jobtracker.backend.model.Application;
import com.jobtracker.backend.model.User;
import com.jobtracker.backend.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    //Get all application for a user
    public List<ApplicationResponse> getAllApplications(User user){
        return applicationRepository
                .findByUserOrderByAppliedDateDesc(user)
                .stream()
                .map(ApplicationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //Create a new application
    public ApplicationResponse createApplication(User user,
                                                 ApplicationRequest request) {
        Application app = new Application();
        app.setUser(user);
        app.setJobTitle(request.getJobTitle());
        app.setCompany(request.getCompany());
        app.setJobUrl(request.getJobUrl());

        if (request.getAppliedDate() != null) {
            app.setAppliedDate(request.getAppliedDate());
        }

        applicationRepository.save(app);
        return ApplicationResponse.fromEntity(app);
    }

    //Update application Status
    public ApplicationResponse updateStatus(User user, UUID id,
                                            Application.Status status) {
        Application app =applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not Found"));

        if (!app.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized");
        }

        app.setStatus(status);
        applicationRepository.save(app);
        return ApplicationResponse.fromEntity(app);
    }

    //Delete an Application
    public void deleteApplication(User user, UUID id) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        applicationRepository.delete(app);
    }
}
