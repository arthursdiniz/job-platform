package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.exception.*;
import com.portfolio.jobplatform.mapper.ApiMapper;
import com.portfolio.jobplatform.repository.JobRepository;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobs; private final CurrentUserService current; private final SkillService skills;
    public JobService(JobRepository jobs, CurrentUserService current, SkillService skills) { this.jobs = jobs; this.current = current; this.skills = skills; }

    @Transactional(readOnly = true)
    public Page<JobResponse> search(String title, Boolean remote, String city, String state, ExperienceLevel level, EmploymentType type, String skill, Pageable pageable) {
        Specification<Job> spec = (root, q, cb) -> cb.and(cb.equal(root.get("status"), JobStatus.OPEN), cb.greaterThan(root.get("expiresAt"), Instant.now()));
        if (title != null && !title.isBlank()) spec = spec.and((r,q,c) -> c.like(c.lower(r.get("title")), "%" + title.trim().toLowerCase() + "%"));
        if (remote != null) spec = spec.and((r,q,c) -> c.equal(r.get("remote"), remote));
        if (city != null && !city.isBlank()) spec = spec.and((r,q,c) -> c.equal(c.lower(r.get("city")), city.trim().toLowerCase()));
        if (state != null && !state.isBlank()) spec = spec.and((r,q,c) -> c.equal(c.lower(r.get("state")), state.trim().toLowerCase()));
        if (level != null) spec = spec.and((r,q,c) -> c.equal(r.get("experienceLevel"), level));
        if (type != null) spec = spec.and((r,q,c) -> c.equal(r.get("employmentType"), type));
        if (skill != null && !skill.isBlank()) spec = spec.and((r,q,c) -> { q.distinct(true); return c.equal(c.lower(r.join("skills", JoinType.INNER).get("name")), skill.trim().toLowerCase()); });
        return jobs.findAll(spec, pageable).map(ApiMapper::job);
    }
    @Transactional(readOnly = true) public JobResponse get(Long id) { return ApiMapper.job(find(id)); }
    @Transactional(readOnly = true)
    public List<JobResponse> companyJobs() {
        return jobs.findByCompanyOrderByCreatedAtDesc(current.company()).stream().map(ApiMapper::job).toList();
    }
    @Transactional public JobResponse create(JobRequest r) {
        validateSalary(r); CompanyProfile company = current.company(); Job job = new Job(); apply(job, r); job.setCompany(company); job.setStatus(JobStatus.OPEN); return ApiMapper.job(jobs.save(job));
    }
    @Transactional public JobResponse update(Long id, JobRequest r) {
        validateSalary(r); Job job = find(id); assertOwner(job); apply(job, r); return ApiMapper.job(jobs.save(job));
    }
    @Transactional public void close(Long id) { Job job = find(id); assertOwner(job); job.setStatus(JobStatus.CLOSED); jobs.save(job); }
    @Transactional public void delete(Long id) {
        Job job = find(id); AppUser user = current.user();
        if (user.getRole() != Role.ADMIN && !job.getCompany().getUser().getId().equals(user.getId())) throw BusinessRuleException.forbidden("You do not own this job");
        jobs.delete(job);
    }
    @Transactional(readOnly = true) public Job find(Long id) { return jobs.findDetailedById(id).orElseThrow(() -> new ResourceNotFoundException("Job not found")); }
    private void assertOwner(Job job) { if (!job.getCompany().getId().equals(current.company().getId())) throw BusinessRuleException.forbidden("You do not own this job"); }
    private void apply(Job j, JobRequest r) {
        j.setTitle(r.title()); j.setDescription(r.description()); j.setCity(r.city()); j.setState(r.state()); j.setCountry(r.country()); j.setRemote(r.remote());
        j.setEmploymentType(r.employmentType()); j.setExperienceLevel(r.experienceLevel()); j.setSalaryMin(r.salaryMin()); j.setSalaryMax(r.salaryMax()); j.setExpiresAt(r.expiresAt()); j.setSkills(skills.resolve(r.skills()));
    }
    private void validateSalary(JobRequest r) { if (r.salaryMin() != null && r.salaryMax() != null && r.salaryMax().compareTo(r.salaryMin()) < 0) throw BusinessRuleException.unprocessable("salaryMax must be greater than or equal to salaryMin"); }
}
