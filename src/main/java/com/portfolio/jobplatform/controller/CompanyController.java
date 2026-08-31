package com.portfolio.jobplatform.controller;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/companies/me") @PreAuthorize("hasRole('COMPANY')")
public class CompanyController {
    private final DashboardService dashboards;
    private final JobService jobs;
    public CompanyController(DashboardService dashboards, JobService jobs) { this.dashboards = dashboards; this.jobs = jobs; }
    @GetMapping("/dashboard") CompanyDashboard dashboard() { return dashboards.company(); }
    @GetMapping("/jobs") java.util.List<JobResponse> jobs() { return jobs.companyJobs(); }
}
