package com.portfolio.jobplatform.controller;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.service.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @PreAuthorize("hasRole('CANDIDATE')")
public class CandidateController {
    private final FavoriteService favorites; private final JobMatchService matches; private final DashboardService dashboards;
    public CandidateController(FavoriteService favorites, JobMatchService matches, DashboardService dashboards) { this.favorites = favorites; this.matches = matches; this.dashboards = dashboards; }
    @PostMapping("/api/jobs/{jobId}/favorite") ResponseEntity<FavoriteResponse> favorite(@PathVariable Long jobId) { return ResponseEntity.status(HttpStatus.CREATED).body(favorites.add(jobId)); }
    @DeleteMapping("/api/jobs/{jobId}/favorite") ResponseEntity<Void> remove(@PathVariable Long jobId) { favorites.remove(jobId); return ResponseEntity.noContent().build(); }
    @GetMapping("/api/candidates/me/favorites") List<FavoriteResponse> favorites() { return favorites.list(); }
    @GetMapping("/api/candidates/me/recommended-jobs") List<RecommendedJobResponse> recommended() { return matches.recommended(); }
    @GetMapping("/api/candidates/me/dashboard") CandidateDashboard dashboard() { return dashboards.candidate(); }
}
