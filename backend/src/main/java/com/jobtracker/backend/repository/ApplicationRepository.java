package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.Application;
import com.jobtracker.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    List<Application> findByUserOrderByAppliedDateDesc(User user);
    long countByUserAndStatus(User user, Application.Status status);
}
