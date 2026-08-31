package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.entity.Skill;
import com.portfolio.jobplatform.repository.SkillRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillService {
    private final SkillRepository repository;
    public SkillService(SkillRepository repository) { this.repository = repository; }
    public Set<Skill> resolve(Set<String> names) {
        if (names == null || names.isEmpty()) return new HashSet<>();
        return names.stream().filter(Objects::nonNull).map(String::trim).filter(s -> !s.isBlank())
                .map(this::resolveOne).collect(Collectors.toCollection(HashSet::new));
    }
    private Skill resolveOne(String name) {
        return repository.findByNameIgnoreCase(name).orElseGet(() -> repository.save(new Skill(toDisplayName(name))));
    }
    private String toDisplayName(String value) {
        if (value.equals(value.toUpperCase(Locale.ROOT))) return value;
        String lower = value.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
