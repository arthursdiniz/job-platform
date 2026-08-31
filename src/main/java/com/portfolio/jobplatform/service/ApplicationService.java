package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.exception.*;
import com.portfolio.jobplatform.mapper.ApiMapper;
import com.portfolio.jobplatform.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.*;

@Service
public class ApplicationService {
    private final ApplicationRepository applications; private final JobService jobService; private final CurrentUserService current;
    public ApplicationService(ApplicationRepository applications, JobService jobService, CurrentUserService current) { this.applications = applications; this.jobService = jobService; this.current = current; }

    @Transactional public ApplicationResponse apply(Long jobId, ApplicationRequest request) {
        CandidateProfile candidate = current.candidate(); Job job = jobService.find(jobId);
        if (job.getStatus() != JobStatus.OPEN) throw BusinessRuleException.unprocessable("Job is closed");
        if (!job.getExpiresAt().isAfter(Instant.now())) throw BusinessRuleException.unprocessable("Job has expired");
        if (applications.existsByCandidateAndJob(candidate, job)) throw BusinessRuleException.conflict("Candidate has already applied to this job");
        JobApplication a = new JobApplication(); a.setCandidate(candidate); a.setJob(job); a.setCoverLetter(request.coverLetter()); a.setStatus(ApplicationStatus.APPLIED);
        return ApiMapper.application(applications.save(a));
    }
    @Transactional(readOnly = true) public List<ApplicationResponse> candidateApplications() {
        return applications.findByCandidateOrderByAppliedAtDesc(current.candidate()).stream().map(ApiMapper::application).toList();
    }
    @Transactional public void cancel(Long id) {
        JobApplication a = find(id); CandidateProfile c = current.candidate();
        if (!a.getCandidate().getId().equals(c.getId())) throw BusinessRuleException.forbidden("You do not own this application");
        if (!EnumSet.of(ApplicationStatus.APPLIED, ApplicationStatus.UNDER_REVIEW).contains(a.getStatus())) throw BusinessRuleException.unprocessable("Application can no longer be cancelled");
        applications.delete(a);
    }
    @Transactional(readOnly = true) public List<ApplicationResponse> jobApplications(Long jobId) {
        Job job = jobService.find(jobId); assertCompanyOwner(job); return applications.findByJobOrderByAppliedAtDesc(job).stream().map(ApiMapper::application).toList();
    }
    @Transactional public ApplicationResponse updateStatus(Long id, ApplicationStatusRequest request) {
        JobApplication a = find(id); assertCompanyOwner(a.getJob()); validateTransition(a.getStatus(), request.status()); a.setStatus(request.status()); return ApiMapper.application(applications.save(a));
    }
    private JobApplication find(Long id) { return applications.findById(id).orElseThrow(() -> new ResourceNotFoundException("Application not found")); }
    private void assertCompanyOwner(Job job) { if (!job.getCompany().getId().equals(current.company().getId())) throw BusinessRuleException.forbidden("This application belongs to another company"); }
    private void validateTransition(ApplicationStatus from, ApplicationStatus to) {
        if (from == to) return;
        Map<ApplicationStatus, Set<ApplicationStatus>> allowed = Map.of(
                ApplicationStatus.APPLIED, EnumSet.of(ApplicationStatus.UNDER_REVIEW, ApplicationStatus.INTERVIEW, ApplicationStatus.REJECTED),
                ApplicationStatus.UNDER_REVIEW, EnumSet.of(ApplicationStatus.INTERVIEW, ApplicationStatus.REJECTED),
                ApplicationStatus.INTERVIEW, EnumSet.of(ApplicationStatus.ACCEPTED, ApplicationStatus.REJECTED),
                ApplicationStatus.REJECTED, EnumSet.noneOf(ApplicationStatus.class),
                ApplicationStatus.ACCEPTED, EnumSet.noneOf(ApplicationStatus.class));
        if (!allowed.get(from).contains(to)) throw BusinessRuleException.unprocessable("Invalid application status transition from " + from + " to " + to);
    }
}
