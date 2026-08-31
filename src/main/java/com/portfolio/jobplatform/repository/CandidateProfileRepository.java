package com.portfolio.jobplatform.repository;
import com.portfolio.jobplatform.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
    Optional<CandidateProfile> findByUserEmailIgnoreCase(String email);
}
