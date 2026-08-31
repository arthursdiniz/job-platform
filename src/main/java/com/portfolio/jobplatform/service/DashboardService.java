package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
    private final CurrentUserService current; private final JobRepository jobs; private final ApplicationRepository applications; private final FavoriteJobRepository favorites;
    public DashboardService(CurrentUserService current, JobRepository jobs, ApplicationRepository applications, FavoriteJobRepository favorites) {
        this.current = current; this.jobs = jobs; this.applications = applications; this.favorites = favorites;
    }
    @Transactional(readOnly = true) public CompanyDashboard company() {
        CompanyProfile c = current.company(); return new CompanyDashboard(jobs.countByCompanyAndStatus(c, JobStatus.OPEN), jobs.countByCompanyAndStatus(c, JobStatus.CLOSED),
                applications.countByCompany(c), applications.countByCompanyAndStatus(c, ApplicationStatus.UNDER_REVIEW), applications.countByCompanyAndStatus(c, ApplicationStatus.INTERVIEW), applications.countByCompanyAndStatus(c, ApplicationStatus.ACCEPTED));
    }
    @Transactional(readOnly = true) public CandidateDashboard candidate() {
        CandidateProfile c = current.candidate(); return new CandidateDashboard(applications.countByCandidate(c), applications.countByCandidateAndStatus(c, ApplicationStatus.UNDER_REVIEW),
                applications.countByCandidateAndStatus(c, ApplicationStatus.INTERVIEW), applications.countByCandidateAndStatus(c, ApplicationStatus.REJECTED), applications.countByCandidateAndStatus(c, ApplicationStatus.ACCEPTED), favorites.countByCandidate(c));
    }
}
