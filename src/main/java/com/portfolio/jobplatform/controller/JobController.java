package com.portfolio.jobplatform.controller;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/jobs")
public class JobController {
    private final JobService service; private final JobMatchService matches;
    public JobController(JobService service, JobMatchService matches) { this.service = service; this.matches = matches; }
    @GetMapping Page<JobResponse> search(@RequestParam(required=false) String title, @RequestParam(required=false) Boolean remote,
            @RequestParam(required=false) String city, @RequestParam(required=false) String state,
            @RequestParam(required=false) ExperienceLevel experienceLevel, @RequestParam(required=false) EmploymentType employmentType,
            @RequestParam(required=false) String skill, @PageableDefault(size=10, sort="createdAt", direction=Sort.Direction.DESC) Pageable pageable) {
        return service.search(title, remote, city, state, experienceLevel, employmentType, skill, pageable);
    }
    @GetMapping("/{id}") JobResponse get(@PathVariable Long id) { return service.get(id); }
    @PostMapping @PreAuthorize("hasRole('COMPANY')") ResponseEntity<JobResponse> create(@Valid @RequestBody JobRequest r) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r)); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('COMPANY')") JobResponse update(@PathVariable Long id, @Valid @RequestBody JobRequest r) { return service.update(id, r); }
    @PatchMapping("/{id}/close") @PreAuthorize("hasRole('COMPANY')") ResponseEntity<Void> close(@PathVariable Long id) { service.close(id); return ResponseEntity.noContent().build(); }
    @DeleteMapping("/{id}") @PreAuthorize("hasAnyRole('COMPANY','ADMIN')") ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
    @GetMapping("/{id}/match") @PreAuthorize("hasRole('CANDIDATE')") MatchResponse match(@PathVariable Long id) { return matches.match(id); }
}
