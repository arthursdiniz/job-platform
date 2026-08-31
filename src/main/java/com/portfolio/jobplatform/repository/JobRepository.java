package com.portfolio.jobplatform.repository;

import com.portfolio.jobplatform.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import java.time.Instant;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findByStatusAndExpiresAtAfter(JobStatus status, Instant now);
    long countByCompanyAndStatus(CompanyProfile company, JobStatus status);
    @EntityGraph(attributePaths = {"company", "skills"})
    List<Job> findByCompanyOrderByCreatedAtDesc(CompanyProfile company);
    @EntityGraph(attributePaths = {"company", "skills"})
    Page<Job> findAll(Specification<Job> specification, Pageable pageable);
    @EntityGraph(attributePaths = {"company", "skills"})
    @Query("select j from Job j where j.id = :id")
    java.util.Optional<Job> findDetailedById(Long id);
}
