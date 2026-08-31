package com.portfolio.jobplatform.controller;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ApplicationController {
    private final ApplicationService service; public ApplicationController(ApplicationService service) { this.service = service; }
    @PostMapping("/api/jobs/{jobId}/applications") @PreAuthorize("hasRole('CANDIDATE')") ResponseEntity<ApplicationResponse> apply(@PathVariable Long jobId, @Valid @RequestBody ApplicationRequest r) { return ResponseEntity.status(HttpStatus.CREATED).body(service.apply(jobId, r)); }
    @GetMapping("/api/candidates/me/applications") @PreAuthorize("hasRole('CANDIDATE')") List<ApplicationResponse> mine() { return service.candidateApplications(); }
    @DeleteMapping("/api/applications/{id}") @PreAuthorize("hasRole('CANDIDATE')") ResponseEntity<Void> cancel(@PathVariable Long id) { service.cancel(id); return ResponseEntity.noContent().build(); }
    @GetMapping("/api/jobs/{jobId}/applications") @PreAuthorize("hasRole('COMPANY')") List<ApplicationResponse> byJob(@PathVariable Long jobId) { return service.jobApplications(jobId); }
    @PatchMapping("/api/applications/{id}/status") @PreAuthorize("hasRole('COMPANY')") ApplicationResponse status(@PathVariable Long id, @Valid @RequestBody ApplicationStatusRequest r) { return service.updateStatus(id, r); }
}
