package com.portfolio.jobplatform.config;

import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Component @Profile("dev")
public class DevelopmentDataSeeder implements CommandLineRunner {
    private final UserRepository users; private final CompanyProfileRepository companies; private final CandidateProfileRepository candidates;
    private final JobRepository jobs; private final SkillRepository skills; private final PasswordEncoder encoder;
    public DevelopmentDataSeeder(UserRepository users, CompanyProfileRepository companies, CandidateProfileRepository candidates,
                                 JobRepository jobs, SkillRepository skills, PasswordEncoder encoder) {
        this.users = users; this.companies = companies; this.candidates = candidates; this.jobs = jobs; this.skills = skills; this.encoder = encoder;
    }
    @Override @Transactional public void run(String... args) {
        if (users.count() > 0) return;
        AppUser companyUser = user("Marina Costa", "company@example.com", Role.COMPANY);
        CompanyProfile company = new CompanyProfile(); company.setUser(companyUser); company.setCompanyName("Tech Harbor"); company.setDescription("Empresa de produtos digitais");
        company.setCity("Santos"); company.setState("SP"); company.setCountry("Brasil"); companies.save(company);
        AppUser candidateUser = user("Alex Silva", "candidate@example.com", Role.CANDIDATE);
        CandidateProfile candidate = new CandidateProfile(); candidate.setUser(candidateUser); candidate.setHeadline("Desenvolvedor Java Backend"); candidate.setExperienceLevel(ExperienceLevel.JUNIOR);
        candidate.setCity("Santos"); candidate.setState("SP"); candidate.setCountry("Brasil"); candidate.setSkills(Set.of(skill("Java"), skill("Spring Boot"), skill("PostgreSQL"))); candidates.save(candidate);
        user("Portfolio Admin", "admin@example.com", Role.ADMIN);
        Job job = new Job(); job.setCompany(company); job.setTitle("Desenvolvedor Java Júnior"); job.setDescription("Desenvolvimento de APIs REST com Spring Boot e PostgreSQL.");
        job.setCity("Santos"); job.setState("SP"); job.setCountry("Brasil"); job.setRemote(true); job.setEmploymentType(EmploymentType.FULL_TIME); job.setExperienceLevel(ExperienceLevel.JUNIOR);
        job.setSalaryMin(new BigDecimal("4500")); job.setSalaryMax(new BigDecimal("6500")); job.setStatus(JobStatus.OPEN); job.setExpiresAt(Instant.now().plus(45, ChronoUnit.DAYS));
        job.setSkills(Set.of(skill("Java"), skill("Spring Boot"), skill("PostgreSQL"), skill("Docker"))); jobs.save(job);
    }
    private AppUser user(String name, String email, Role role) { AppUser u = new AppUser(); u.setName(name); u.setEmail(email); u.setPassword(encoder.encode("Portfolio123!")); u.setRole(role); return users.save(u); }
    private Skill skill(String name) { return skills.findByNameIgnoreCase(name).orElseGet(() -> skills.save(new Skill(name))); }
}
