package com.portfolio.jobplatform.mapper;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.entity.*;
import java.util.Set;
import java.util.stream.Collectors;

public final class ApiMapper {
    private ApiMapper() { }
    public static Set<String> skillNames(Set<Skill> skills) { return skills.stream().map(Skill::getName).collect(Collectors.toCollection(java.util.TreeSet::new)); }
    public static JobResponse job(Job j) {
        return new JobResponse(j.getId(), j.getTitle(), j.getDescription(), new CompanySummary(j.getCompany().getId(), j.getCompany().getCompanyName()),
                j.getCity(), j.getState(), j.getCountry(), j.isRemote(), j.getEmploymentType(), j.getExperienceLevel(), j.getSalaryMin(), j.getSalaryMax(),
                j.getStatus(), skillNames(j.getSkills()), j.getCreatedAt(), j.getUpdatedAt(), j.getExpiresAt());
    }
    public static CandidateSummary candidate(CandidateProfile c) {
        return new CandidateSummary(c.getId(), c.getUser().getName(), c.getHeadline(), c.getExperienceLevel(), skillNames(c.getSkills()));
    }
    public static ApplicationResponse application(JobApplication a) {
        return new ApplicationResponse(a.getId(), job(a.getJob()), candidate(a.getCandidate()), a.getStatus(), a.getCoverLetter(), a.getAppliedAt(), a.getUpdatedAt());
    }
    public static CandidateProfileResponse candidateProfile(CandidateProfile c) {
        return new CandidateProfileResponse(c.getId(), c.getUser().getId(), c.getUser().getName(), c.getUser().getEmail(), c.getHeadline(), c.getBio(),
                c.getCity(), c.getState(), c.getCountry(), c.getExperienceLevel(), c.getLinkedinUrl(), c.getGithubUrl(), c.getPortfolioUrl(), skillNames(c.getSkills()),
                c.getCreatedAt(), c.getUpdatedAt());
    }
    public static CompanyProfileResponse companyProfile(CompanyProfile c) {
        return new CompanyProfileResponse(c.getId(), c.getUser().getId(), c.getUser().getName(), c.getUser().getEmail(), c.getCompanyName(), c.getDescription(),
                c.getWebsite(), c.getCity(), c.getState(), c.getCountry(), c.getCreatedAt(), c.getUpdatedAt());
    }
}
