package com.portfolio.jobplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Entity @Table(name = "jobs")
public class Job extends BaseEntity {
    @Column(nullable = false, length = 180) private String title;
    @Column(nullable = false, columnDefinition = "text") private String description;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "company_id", nullable = false) private CompanyProfile company;
    @Column(length = 100) private String city;
    @Column(length = 80) private String state;
    @Column(length = 80) private String country;
    @Column(nullable = false) private boolean remote;
    @Enumerated(EnumType.STRING) @Column(name = "employment_type", nullable = false, length = 20) private EmploymentType employmentType;
    @Enumerated(EnumType.STRING) @Column(name = "experience_level", nullable = false, length = 20) private ExperienceLevel experienceLevel;
    @Column(name = "salary_min", precision = 14, scale = 2) private BigDecimal salaryMin;
    @Column(name = "salary_max", precision = 14, scale = 2) private BigDecimal salaryMax;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private JobStatus status = JobStatus.OPEN;
    @Column(name = "expires_at", nullable = false) private Instant expiresAt;
    @ManyToMany
    @JoinTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();
}
