package com.convodesk.backend.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String email;

    @Column(nullable=false)
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    private Role role;

    // ---- Multi-Tenant Logic ----
    // Super Admin => businessId = null
    // Business Admin => businessId = <id>

    @ManyToOne
    @JoinColumn(name="business_id")
    private Business business;
    private Boolean active=true;
    private Boolean emailVerified = false;
    private LocalDateTime createdAt = LocalDateTime.now();

}