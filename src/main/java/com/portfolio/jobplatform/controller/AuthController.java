package com.portfolio.jobplatform.controller;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service; public AuthController(AuthService service) { this.service = service; }
    @PostMapping("/register/candidate") ResponseEntity<AuthResponse> candidate(@Valid @RequestBody CandidateRegisterRequest r) { return ResponseEntity.status(HttpStatus.CREATED).body(service.registerCandidate(r)); }
    @PostMapping("/register/company") ResponseEntity<AuthResponse> company(@Valid @RequestBody CompanyRegisterRequest r) { return ResponseEntity.status(HttpStatus.CREATED).body(service.registerCompany(r)); }
    @PostMapping("/login") AuthResponse login(@Valid @RequestBody LoginRequest r) { return service.login(r); }
}
