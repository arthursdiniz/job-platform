package com.portfolio.jobplatform.repository;
import com.portfolio.jobplatform.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByNameIgnoreCase(String name);
    List<Skill> findAllByNameIn(Collection<String> names);
}
