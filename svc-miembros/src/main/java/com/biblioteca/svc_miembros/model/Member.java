package com.biblioteca.svc_miembros.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "members")
public class Member {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "member_type")
    private String memberType;

    @Column(name = "registration_date")
    private String registrationDate;

    @Column(name = "active_loans")
    private Integer activeLoans;
}
