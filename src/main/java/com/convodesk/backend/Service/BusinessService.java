package com.convodesk.backend.Service;

import com.convodesk.backend.Dto.BusinessProfileRequest;
import com.convodesk.backend.Entity.Business;
import com.convodesk.backend.Repository.BusinessRepository;
import com.convodesk.backend.Security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;

    public Business getMyBusiness() {
        Long tenantId = TenantContext.getTenantId();
        return businessRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Business not found"));
    }

    public Business updateMyBusiness(BusinessProfileRequest req) {
        Long tenantId = TenantContext.getTenantId();
        Business business = businessRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        business.setBusinessName(req.getBusinessName());
        business.setPhone(req.getPhone());
        business.setAddress(req.getAddress());
        business.setTimezone(req.getTimezone());

        return businessRepository.save(business);
    }
}