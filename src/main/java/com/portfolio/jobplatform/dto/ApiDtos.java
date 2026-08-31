package com.portfolio.jobplatform.dto;

import com.portfolio.jobplatform.entity.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public final class ApiDtos {
    private ApiDtos() { }

    public record CandidateRegisterRequest(
            @NotBlank @Size(max = 120) String name,
            @NotBlank @Email @Size(max = 180) String email,
            @NotBlank @Size(min = 8, max = 72) String password,
            @Size(max = 160) String headline,
            ExperienceLevel experienceLevel) { }

    public record CompanyRegisterRequest(
            @NotBlank @Size(max = 120) String ownerName,
            @NotBlank @Email @Size(max = 180) String email,
            @NotBlank @Size(min = 8, max = 72) String password,
            @NotBlank @Size(max = 160) String companyName) { }

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) { }
    public record UserSummary(Long id, String name, String email, Role role) { }
    public record AuthResponse(String accessToken, String tokenType, long expiresIn, UserSummary user) { }

    public record CandidateProfileRequest(
            @Size(max = 160) String headline, @Size(max = 4000) String bio,
            @Size(max = 100) String city, @Size(max = 80) String state, @Size(max = 80) String country,
            ExperienceLevel experienceLevel,
            @URL @Size(max = 300) String linkedinUrl,
            @URL @Size(max = 300) String githubUrl,
            @URL @Size(max = 300) String portfolioUrl,
            Set<@NotBlank @Size(max = 80) String> skills) { }

    public record CandidateProfileResponse(Long id, Long userId, String name, String email, String headline,
            String bio, String city, String state, String country, ExperienceLevel experienceLevel,
            String linkedinUrl, String githubUrl, String portfolioUrl, Set<String> skills,
            Instant createdAt, Instant updatedAt) { }

    public record CompanyProfileRequest(@NotBlank @Size(max = 160) String companyName,
            @Size(max = 4000) String description, @URL @Size(max = 300) String website,
            @Size(max = 100) String city, @Size(max = 80) String state, @Size(max = 80) String country) { }

    public record CompanySummary(Long id, String companyName) { }
    public record CompanyProfileResponse(Long id, Long userId, String ownerName, String email, String companyName,
            String description, String website, String city, String state, String country,
            Instant createdAt, Instant updatedAt) { }

    public record JobRequest(@NotBlank @Size(max = 180) String title, @NotBlank @Size(max = 10000) String description,
            @Size(max = 100) String city, @Size(max = 80) String state, @Size(max = 80) String country,
            boolean remote, @NotNull EmploymentType employmentType, @NotNull ExperienceLevel experienceLevel,
            @PositiveOrZero BigDecimal salaryMin, @PositiveOrZero BigDecimal salaryMax,
            @NotNull @Future Instant expiresAt, Set<@NotBlank @Size(max = 80) String> skills) { }

    public record JobResponse(Long id, String title, String description, CompanySummary company,
            String city, String state, String country, boolean remote, EmploymentType employmentType,
            ExperienceLevel experienceLevel, BigDecimal salaryMin, BigDecimal salaryMax, JobStatus status,
            Set<String> skills, Instant createdAt, Instant updatedAt, Instant expiresAt) { }

    public record ApplicationRequest(@Size(max = 5000) String coverLetter) { }
    public record ApplicationStatusRequest(@NotNull ApplicationStatus status) { }
    public record CandidateSummary(Long id, String name, String headline, ExperienceLevel experienceLevel, Set<String> skills) { }
    public record ApplicationResponse(Long id, JobResponse job, CandidateSummary candidate, ApplicationStatus status,
            String coverLetter, Instant appliedAt, Instant updatedAt) { }

    public record FavoriteResponse(Long id, JobResponse job, Instant createdAt) { }
    public record MatchResponse(Long jobId, Long candidateId, int matchScore, Set<String> matchedSkills, Set<String> missingSkills) { }
    public record RecommendedJobResponse(JobResponse job, int matchScore, Set<String> matchedSkills, Set<String> missingSkills) { }
    public record CompanyDashboard(long openJobs, long closedJobs, long totalApplications, long underReview, long interviews, long accepted) { }
    public record CandidateDashboard(long totalApplications, long underReview, long interviews, long rejected, long accepted, long favoriteJobs) { }
}
