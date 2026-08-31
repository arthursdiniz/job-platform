package com.portfolio.jobplatform.repository;
import com.portfolio.jobplatform.entity.*;
import org.springframework.data.jpa.repository.*;
import java.util.List;
public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByCandidateAndJob(CandidateProfile candidate, Job job);
    List<JobApplication> findByCandidateOrderByAppliedAtDesc(CandidateProfile candidate);
    List<JobApplication> findByJobOrderByAppliedAtDesc(Job job);
    long countByCandidate(CandidateProfile candidate);
    long countByCandidateAndStatus(CandidateProfile candidate, ApplicationStatus status);
    @Query("select count(a) from JobApplication a where a.job.company = :company") long countByCompany(CompanyProfile company);
    @Query("select count(a) from JobApplication a where a.job.company = :company and a.status = :status") long countByCompanyAndStatus(CompanyProfile company, ApplicationStatus status);
}
