package com.portfolio.jobplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Entity @Table(name = "candidate_profiles")
public class CandidateProfile extends BaseEntity {
    @OneToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false, unique = true) private AppUser user;
    @Column(length = 160) private String headline;
    @Column(columnDefinition = "text") private String bio;
    @Column(length = 100) private String city;
    @Column(length = 80) private String state;
    @Column(length = 80) private String country;
    @Enumerated(EnumType.STRING) @Column(name = "experience_level", length = 20) private ExperienceLevel experienceLevel;
    @Column(name = "linkedin_url", length = 300) private String linkedinUrl;
    @Column(name = "github_url", length = 300) private String githubUrl;
    @Column(name = "portfolio_url", length = 300) private String portfolioUrl;
    @ManyToMany
    @JoinTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "candidate_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();
}
