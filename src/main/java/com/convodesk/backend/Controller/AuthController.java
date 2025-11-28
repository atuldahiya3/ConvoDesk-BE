package com.convodesk.backend.Controller;

import Dto.AuthResponse;
import Dto.LoginRequest;
import Dto.RegisterBusinessRequest;
import com.convodesk.backend.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-business")
    public AuthResponse registerBusiness(@RequestBody RegisterBusinessRequest req) {
        return authService.registerBusiness(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/login-superadmin")
    public AuthResponse loginSuperadmin(@RequestBody LoginRequest req) {
        return authService.superAdminLogin(req);
    }
}
