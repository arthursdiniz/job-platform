package com.portfolio.jobplatform.controller;

import com.portfolio.jobplatform.dto.ApiDtos.UserSummary;
import com.portfolio.jobplatform.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/admin") @PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository users; public AdminController(UserRepository users) { this.users = users; }
    @GetMapping("/users") Page<UserSummary> users(@PageableDefault(size=20, sort="createdAt", direction=Sort.Direction.DESC) Pageable pageable) {
        return users.findAll(pageable).map(u -> new UserSummary(u.getId(), u.getName(), u.getEmail(), u.getRole()));
    }
}
