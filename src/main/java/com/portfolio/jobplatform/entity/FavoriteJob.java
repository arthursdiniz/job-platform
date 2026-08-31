package com.portfolio.jobplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter @Setter
@Entity @Table(name = "favorite_jobs", uniqueConstraints = @UniqueConstraint(name = "uk_favorite_candidate_job", columnNames = {"candidate_id", "job_id"}))
public class FavoriteJob {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "candidate_id", nullable = false) private CandidateProfile candidate;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "job_id", nullable = false) private Job job;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @PrePersist void timestamp() { createdAt = Instant.now(); }
}
