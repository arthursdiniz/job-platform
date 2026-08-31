package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.ApplicationRequest;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.exception.BusinessRuleException;
import com.portfolio.jobplatform.repository.ApplicationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock ApplicationRepository repository; @Mock JobService jobs; @Mock CurrentUserService current;
    @InjectMocks ApplicationService service;
    CandidateProfile candidate; Job job;
    @BeforeEach void setup() {
        candidate = new CandidateProfile(); candidate.setId(1L); candidate.setUser(user());
        CompanyProfile company = new CompanyProfile(); company.setId(2L); company.setCompanyName("Acme"); company.setUser(user());
        job = new Job(); job.setId(10L); job.setCompany(company); job.setTitle("Java"); job.setDescription("Backend"); job.setStatus(JobStatus.OPEN);
        job.setExpiresAt(Instant.now().plusSeconds(3600)); job.setEmploymentType(EmploymentType.FULL_TIME); job.setExperienceLevel(ExperienceLevel.JUNIOR);
        when(current.candidate()).thenReturn(candidate); when(jobs.find(10L)).thenReturn(job);
    }
    @Test void candidateCanApply() {
        when(repository.save(any())).thenAnswer(i -> { JobApplication a = i.getArgument(0); a.setId(99L); a.setAppliedAt(Instant.now()); a.setUpdatedAt(Instant.now()); return a; });
        var result = service.apply(10L, new ApplicationRequest("Olá"));
        assertThat(result.status()).isEqualTo(ApplicationStatus.APPLIED); verify(repository).save(any(JobApplication.class));
    }
    @Test void duplicateApplicationFails() {
        when(repository.existsByCandidateAndJob(candidate, job)).thenReturn(true);
        assertThatThrownBy(() -> service.apply(10L, new ApplicationRequest(null))).isInstanceOf(BusinessRuleException.class).hasMessageContaining("already applied");
    }
    @Test void closedJobFails() {
        job.setStatus(JobStatus.CLOSED);
        assertThatThrownBy(() -> service.apply(10L, new ApplicationRequest(null))).isInstanceOf(BusinessRuleException.class).hasMessageContaining("closed");
    }
    @Test void expiredJobFails() {
        job.setExpiresAt(Instant.now().minusSeconds(1));
        assertThatThrownBy(() -> service.apply(10L, new ApplicationRequest(null))).isInstanceOf(BusinessRuleException.class).hasMessageContaining("expired");
    }
    private AppUser user() { AppUser u = new AppUser(); u.setId(3L); u.setName("User"); u.setEmail("u@example.com"); u.setRole(Role.CANDIDATE); return u; }
}
