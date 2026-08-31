package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.mapper.ApiMapper;
import com.portfolio.jobplatform.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobMatchService {
    private final CurrentUserService current; private final JobService jobs; private final JobRepository jobRepository;
    public JobMatchService(CurrentUserService current, JobService jobs, JobRepository jobRepository) { this.current = current; this.jobs = jobs; this.jobRepository = jobRepository; }
    @Transactional(readOnly = true) public MatchResponse match(Long jobId) { return calculate(current.candidate(), jobs.find(jobId)); }
    @Transactional(readOnly = true) public List<RecommendedJobResponse> recommended() {
        CandidateProfile c = current.candidate();
        return jobRepository.findByStatusAndExpiresAtAfter(JobStatus.OPEN, Instant.now()).stream().map(j -> {
            MatchResponse m = calculate(c, j); return new RecommendedJobResponse(ApiMapper.job(j), m.matchScore(), m.matchedSkills(), m.missingSkills());
        }).sorted(Comparator.comparingInt(RecommendedJobResponse::matchScore).reversed()).toList();
    }
    MatchResponse calculate(CandidateProfile candidate, Job job) {
        Set<String> candidateSkills = candidate.getSkills().stream().map(s -> s.getName().toLowerCase()).collect(Collectors.toSet());
        Set<String> matched = new TreeSet<>(); Set<String> missing = new TreeSet<>();
        for (Skill s : job.getSkills()) { if (candidateSkills.contains(s.getName().toLowerCase())) matched.add(s.getName()); else missing.add(s.getName()); }
        int skillScore = job.getSkills().isEmpty() ? 70 : (int) Math.round(70.0 * matched.size() / job.getSkills().size());
        int experienceScore = experienceScore(candidate.getExperienceLevel(), job.getExperienceLevel());
        int locationScore = locationScore(candidate, job);
        int remoteScore = job.isRemote() ? 5 : 0;
        return new MatchResponse(job.getId(), candidate.getId(), Math.min(100, skillScore + experienceScore + locationScore + remoteScore), matched, missing);
    }
    private int experienceScore(ExperienceLevel candidate, ExperienceLevel required) {
        if (candidate == null || required == null) return 0;
        int difference = candidate.ordinal() - required.ordinal(); return difference >= 0 ? 15 : difference == -1 ? 8 : 0;
    }
    private int locationScore(CandidateProfile c, Job j) {
        if (same(c.getCity(), j.getCity()) && same(c.getState(), j.getState())) return 10;
        if (same(c.getState(), j.getState())) return 6;
        if (same(c.getCountry(), j.getCountry())) return 3;
        return 0;
    }
    private boolean same(String a, String b) { return a != null && b != null && !a.isBlank() && a.equalsIgnoreCase(b); }
}
