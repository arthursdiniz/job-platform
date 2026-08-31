package com.portfolio.jobplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "company_profiles")
public class CompanyProfile extends BaseEntity {
    @OneToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false, unique = true) private AppUser user;
    @Column(name = "company_name", nullable = false, length = 160) private String companyName;
    @Column(columnDefinition = "text") private String description;
    @Column(length = 300) private String website;
    @Column(length = 100) private String city;
    @Column(length = 80) private String state;
    @Column(length = 80) private String country;
}
