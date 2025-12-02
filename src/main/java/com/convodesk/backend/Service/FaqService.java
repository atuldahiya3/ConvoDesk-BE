package com.convodesk.backend.Service;


import com.convodesk.backend.Dto.FaqRequest;
import com.convodesk.backend.Entity.Business;
import com.convodesk.backend.Entity.FAQ;
import com.convodesk.backend.Repository.BusinessRepository;
import com.convodesk.backend.Repository.FaqRepository;
import com.convodesk.backend.Security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;
    private final BusinessRepository businessRepository;

    // --------- Create FAQ ---------
    public FAQ createFaq(FaqRequest req) {
        Long tenantId = TenantContext.getTenantId();

        Business business = businessRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        FAQ faq = FAQ.builder()
                .business(business)
                .question(req.getQuestion())
                .answer(req.getAnswer())
                .build();

        return faqRepository.save(faq);
    }

    // --------- Get all FAQs for tenant ---------
    public List<FAQ> getAllFaqs() {
        Long tenantId = TenantContext.getTenantId();
        return faqRepository.findByBusinessId(tenantId);
    }

    // --------- Delete FAQ ---------
    public void deleteFaq(Long id) {
        Long tenantId = TenantContext.getTenantId();
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));

        if (!faq.getBusiness().getId().equals(tenantId)) {
            throw new RuntimeException("Not allowed");
        }

        faqRepository.delete(faq);
    }
}