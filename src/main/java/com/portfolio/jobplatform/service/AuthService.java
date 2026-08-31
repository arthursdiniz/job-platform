package com.portfolio.jobplatform.service;

import com.portfolio.jobplatform.dto.ApiDtos.*;
import com.portfolio.jobplatform.entity.*;
import com.portfolio.jobplatform.exception.BusinessRuleException;
import com.portfolio.jobplatform.repository.*;
import com.portfolio.jobplatform.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users; private final CandidateProfileRepository candidates; private final CompanyProfileRepository companies;
    private final PasswordEncoder encoder; private final AuthenticationManager authManager; private final JwtService jwt;
    public AuthService(UserRepository users, CandidateProfileRepository candidates, CompanyProfileRepository companies,
                       PasswordEncoder encoder, AuthenticationManager authManager, JwtService jwt) {
        this.users = users; this.candidates = candidates; this.companies = companies; this.encoder = encoder; this.authManager = authManager; this.jwt = jwt;
    }
    @Transactional
    public AuthResponse registerCandidate(CandidateRegisterRequest request) {
        AppUser user = createUser(request.name(), request.email(), request.password(), Role.CANDIDATE);
        CandidateProfile profile = new CandidateProfile(); profile.setUser(user); profile.setHeadline(request.headline()); profile.setExperienceLevel(request.experienceLevel());
        candidates.save(profile); return response(user);
    }
    @Transactional
    public AuthResponse registerCompany(CompanyRegisterRequest request) {
        AppUser user = createUser(request.ownerName(), request.email(), request.password(), Role.COMPANY);
        CompanyProfile profile = new CompanyProfile(); profile.setUser(user); profile.setCompanyName(request.companyName()); companies.save(profile); return response(user);
    }
    public AuthResponse login(LoginRequest request) {
        try { authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password())); }
        catch (AuthenticationException ex) { throw new BusinessRuleException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid email or password"); }
        AppUser user = users.findByEmailIgnoreCase(request.email()).orElseThrow(); return response(user);
    }
    private AppUser createUser(String name, String email, String password, Role role) {
        String normalized = email.trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(normalized)) throw BusinessRuleException.conflict("Email is already registered");
        AppUser user = new AppUser(); user.setName(name.trim()); user.setEmail(normalized); user.setPassword(encoder.encode(password)); user.setRole(role); return users.save(user);
    }
    private AuthResponse response(AppUser user) {
        return new AuthResponse(jwt.generate(user), "Bearer", jwt.getExpirationSeconds(), new UserSummary(user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }
}
