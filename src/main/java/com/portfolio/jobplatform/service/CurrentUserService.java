package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.exception.ResourceNotFoundException;
import com.portfolio.jobplatform.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository users;
    private final CandidateProfileRepository candidates;
    private final CompanyProfileRepository companies;
    public CurrentUserService(UserRepository users, CandidateProfileRepository candidates, CompanyProfileRepository companies) {
        this.users = users; this.candidates = candidates; this.companies = companies;
    }
    public String email() { return SecurityContextHolder.getContext().getAuthentication().getName(); }
    public AppUser user() { return users.findByEmailIgnoreCase(email()).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    public CandidateProfile candidate() { return candidates.findByUserEmailIgnoreCase(email()).orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found")); }
    public CompanyProfile company() { return companies.findByUserEmailIgnoreCase(email()).orElseThrow(() -> new ResourceNotFoundException("Company profile not found")); }
}
