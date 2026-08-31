package com.portfolio.jobplatform.controller;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {
    private final ProfileService service; public ProfileController(ProfileService service) { this.service = service; }
    @GetMapping("/api/candidates/me/profile") @PreAuthorize("hasRole('CANDIDATE')") CandidateProfileResponse candidate() { return service.candidateMe(); }
    @PutMapping("/api/candidates/me/profile") @PreAuthorize("hasRole('CANDIDATE')") CandidateProfileResponse updateCandidate(@Valid @RequestBody CandidateProfileRequest r) { return service.updateCandidate(r); }
    @GetMapping("/api/companies/me/profile") @PreAuthorize("hasRole('COMPANY')") CompanyProfileResponse company() { return service.companyMe(); }
    @PutMapping("/api/companies/me/profile") @PreAuthorize("hasRole('COMPANY')") CompanyProfileResponse updateCompany(@Valid @RequestBody CompanyProfileRequest r) { return service.updateCompany(r); }
}
