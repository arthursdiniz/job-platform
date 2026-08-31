package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.FavoriteResponse;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.exception.*;
import com.portfolio.jobplatform.mapper.ApiMapper;
import com.portfolio.jobplatform.repository.FavoriteJobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteJobRepository favorites; private final CurrentUserService current; private final JobService jobs;
    public FavoriteService(FavoriteJobRepository favorites, CurrentUserService current, JobService jobs) { this.favorites = favorites; this.current = current; this.jobs = jobs; }
    @Transactional public FavoriteResponse add(Long jobId) {
        CandidateProfile c = current.candidate(); Job j = jobs.find(jobId);
        if (favorites.existsByCandidateAndJob(c, j)) throw BusinessRuleException.conflict("Job is already in favorites");
        FavoriteJob f = new FavoriteJob(); f.setCandidate(c); f.setJob(j); f = favorites.save(f); return new FavoriteResponse(f.getId(), ApiMapper.job(j), f.getCreatedAt());
    }
    @Transactional public void remove(Long jobId) {
        CandidateProfile c = current.candidate(); Job j = jobs.find(jobId);
        FavoriteJob f = favorites.findByCandidateAndJob(c, j).orElseThrow(() -> new ResourceNotFoundException("Favorite not found")); favorites.delete(f);
    }
    @Transactional(readOnly = true) public List<FavoriteResponse> list() {
        return favorites.findByCandidateOrderByCreatedAtDesc(current.candidate()).stream().map(f -> new FavoriteResponse(f.getId(), ApiMapper.job(f.getJob()), f.getCreatedAt())).toList();
    }
}
