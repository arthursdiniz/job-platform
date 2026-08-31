package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.repository.JobRepository;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class JobMatchServiceTest {
    private final JobMatchService service = new JobMatchService(mock(CurrentUserService.class), mock(JobService.class), mock(JobRepository.class));
    @Test void calculatesScoreAndSkillSets() {
        CandidateProfile c = new CandidateProfile(); c.setId(5L); c.setExperienceLevel(ExperienceLevel.JUNIOR); c.setCity("Santos"); c.setState("SP"); c.setCountry("Brasil");
        c.setSkills(Set.of(new Skill("Java"), new Skill("Spring Boot"), new Skill("PostgreSQL"), new Skill("Docker")));
        Job j = new Job(); j.setId(10L); j.setExperienceLevel(ExperienceLevel.JUNIOR); j.setCity("Santos"); j.setState("SP"); j.setCountry("Brasil"); j.setRemote(true);
        j.setSkills(Set.of(new Skill("Java"), new Skill("Spring Boot"), new Skill("PostgreSQL"), new Skill("AWS")));
        var result = service.calculate(c, j);
        assertThat(result.matchScore()).isEqualTo(83);
        assertThat(result.matchedSkills()).containsExactly("Java", "PostgreSQL", "Spring Boot");
        assertThat(result.missingSkills()).containsExactly("AWS");
    }
}
