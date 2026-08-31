package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.mapper.ApiMapper;
import com.portfolio.jobplatform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    private final CurrentUserService current; private final CandidateProfileRepository candidates; private final CompanyProfileRepository companies; private final SkillService skills;
    public ProfileService(CurrentUserService current, CandidateProfileRepository candidates, CompanyProfileRepository companies, SkillService skills) {
        this.current = current; this.candidates = candidates; this.companies = companies; this.skills = skills;
    }
    @Transactional(readOnly = true) public CandidateProfileResponse candidateMe() { return ApiMapper.candidateProfile(current.candidate()); }
    @Transactional public CandidateProfileResponse updateCandidate(CandidateProfileRequest r) {
        CandidateProfile c = current.candidate(); c.setHeadline(r.headline()); c.setBio(r.bio()); c.setCity(r.city()); c.setState(r.state()); c.setCountry(r.country());
        c.setExperienceLevel(r.experienceLevel()); c.setLinkedinUrl(r.linkedinUrl()); c.setGithubUrl(r.githubUrl()); c.setPortfolioUrl(r.portfolioUrl()); c.setSkills(skills.resolve(r.skills()));
        return ApiMapper.candidateProfile(candidates.save(c));
    }
    @Transactional(readOnly = true) public CompanyProfileResponse companyMe() { return ApiMapper.companyProfile(current.company()); }
    @Transactional public CompanyProfileResponse updateCompany(CompanyProfileRequest r) {
        CompanyProfile c = current.company(); c.setCompanyName(r.companyName()); c.setDescription(r.description()); c.setWebsite(r.website()); c.setCity(r.city()); c.setState(r.state()); c.setCountry(r.country());
        return ApiMapper.companyProfile(companies.save(c));
    }
}
