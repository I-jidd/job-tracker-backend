package com.jobtracker.backend.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String company;

    private String jobUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.APPLIED;

    private LocalDate appliedDate = LocalDate.now();

    private Integer matchScore;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    public enum Status {
        APPLIED, INTERVIEW, REJECTED, ACCEPTED
    }
}
