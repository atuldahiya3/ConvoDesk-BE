package com.convodesk.backend.Service;

import com.convodesk.backend.Dto.AuthResponse;
import com.convodesk.backend.Dto.LoginRequest;
import com.convodesk.backend.Dto.RegisterBusinessRequest;
import com.convodesk.backend.Entity.Business;
import com.convodesk.backend.Entity.Role;
import com.convodesk.backend.Entity.User;
import com.convodesk.backend.Repository.BusinessRepository;
import com.convodesk.backend.Repository.UserRepository;
import com.convodesk.backend.Security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // ---------- Register Business + Business Admin ----------
    public AuthResponse registerBusiness(RegisterBusinessRequest req) {

        // Check duplicate user
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Create Business
        Business business = Business.builder()
                .businessName(req.getBusinessName())
                .phone(req.getPhone())
                .address(req.getAddress())
                .build();

        businessRepository.save(business);

        // Create Business Admin
        User user = User.builder()
                .email(req.getEmail())
                .hashedPassword(passwordEncoder.encode(req.getPassword()))
                .role(Role.BUSINESS_ADMIN)
                .business(business)
                .active(true)
                .build();

        userRepository.save(user);

        // Generate token
        String token = jwtProvider.generateToken(user);

        return new AuthResponse(token, user.getRole().name());
    }

    // ------------------- Login -------------------
    public AuthResponse login(LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtProvider.generateToken(user);

        return new AuthResponse(token, user.getRole().name());
    }

    // ----------- Super Admin Login -----------
    public AuthResponse superAdminLogin(LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.SUPER_ADMIN) {
            throw new RuntimeException("Not a super admin");
        }

        String token = jwtProvider.generateToken(user);

        return new AuthResponse(token, user.getRole().name());
    }
}
