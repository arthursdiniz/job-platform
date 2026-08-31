package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.JobRequest;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.exception.BusinessRuleException;
import com.portfolio.jobplatform.repository.JobRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {
    @Mock JobRepository repository; @Mock CurrentUserService current; @Mock SkillService skills; @InjectMocks JobService service;
    @Test void companyCannotUpdateAnotherCompanyJob() {
        CompanyProfile owner = company(1L); CompanyProfile attacker = company(2L); Job job = new Job(); job.setId(10L); job.setCompany(owner);
        when(repository.findDetailedById(10L)).thenReturn(Optional.of(job)); when(current.company()).thenReturn(attacker);
        JobRequest request = new JobRequest("Java", "Description", "Santos", "SP", "Brasil", true, EmploymentType.FULL_TIME,
                ExperienceLevel.JUNIOR, null, null, Instant.now().plusSeconds(3600), Set.of("Java"));
        assertThatThrownBy(() -> service.update(10L, request)).isInstanceOf(BusinessRuleException.class).hasMessageContaining("do not own");
        verify(repository, never()).save(any());
    }
    @Test void companyJobsUsesAuthenticatedCompanyAndMapsResults() {
        CompanyProfile authenticated = company(7L); authenticated.setCompanyName("Tech Harbor");
        Job job = new Job(); job.setId(11L); job.setTitle("Backend Java"); job.setCompany(authenticated); job.setSkills(new HashSet<>());
        when(current.company()).thenReturn(authenticated);
        when(repository.findByCompanyOrderByCreatedAtDesc(authenticated)).thenReturn(List.of(job));

        var result = service.companyJobs();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().title()).isEqualTo("Backend Java");
        verify(repository).findByCompanyOrderByCreatedAtDesc(authenticated);
    }
    private CompanyProfile company(Long id) { CompanyProfile c = new CompanyProfile(); c.setId(id); AppUser u = new AppUser(); u.setId(id); c.setUser(u); return c; }
}
