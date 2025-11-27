package com.convodesk.backend.Controller;

import com.convodesk.backend.Security.TenantContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @GetMapping("/me")
    public String getMyBusiness() {
        Long tenantId = TenantContext.getTenantId();
        return "This request belongs to businessId: " + tenantId;
    }
}
