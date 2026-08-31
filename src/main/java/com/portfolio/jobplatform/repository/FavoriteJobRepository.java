package com.portfolio.jobplatform.repository;
import com.portfolio.jobplatform.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface FavoriteJobRepository extends JpaRepository<FavoriteJob, Long> {
    boolean existsByCandidateAndJob(CandidateProfile candidate, Job job);
    Optional<FavoriteJob> findByCandidateAndJob(CandidateProfile candidate, Job job);
    List<FavoriteJob> findByCandidateOrderByCreatedAtDesc(CandidateProfile candidate);
    long countByCandidate(CandidateProfile candidate);
}
