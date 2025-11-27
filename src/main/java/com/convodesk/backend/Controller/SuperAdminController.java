package com.convodesk.backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {

    @GetMapping("/all-businesses")
    public String getAll() {
        return "Only super admin can access this.";
    }
}