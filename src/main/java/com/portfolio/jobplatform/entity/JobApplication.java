package com.portfolio.jobplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter @Setter
@Entity @Table(name = "applications", uniqueConstraints = @UniqueConstraint(name = "uk_application_candidate_job", columnNames = {"candidate_id", "job_id"}))
public class JobApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "candidate_id", nullable = false) private CandidateProfile candidate;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "job_id", nullable = false) private Job job;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private ApplicationStatus status = ApplicationStatus.APPLIED;
    @Column(name = "cover_letter", columnDefinition = "text") private String coverLetter;
    @Column(name = "applied_at", nullable = false) private Instant appliedAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @PrePersist void createTimestamps() { appliedAt = Instant.now(); updatedAt = appliedAt; }
    @PreUpdate void updateTimestamp() { updatedAt = Instant.now(); }
}
