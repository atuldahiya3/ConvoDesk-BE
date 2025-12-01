package com.convodesk.backend.Controller;

import com.convodesk.backend.Dto.BusinessProfileRequest;
import com.convodesk.backend.Dto.FaqRequest;
import com.convodesk.backend.Entity.Business;
import com.convodesk.backend.Entity.FAQ;
import com.convodesk.backend.Security.TenantContext;
import com.convodesk.backend.Service.BusinessService;
import com.convodesk.backend.Service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final FaqService faqService;

    // ---------- Business Profile ----------
    @GetMapping("/profile")
    public Business getMyBusiness() {
        return businessService.getMyBusiness();
    }

    @PutMapping("/profile")
    public Business updateBusiness(@RequestBody BusinessProfileRequest req) {
        return businessService.updateMyBusiness(req);
    }

    // ---------- FAQ CRUD ----------
    @PostMapping("/faq")
    public FAQ createFaq(@RequestBody FaqRequest req) {
        return faqService.createFaq(req);
    }

    @GetMapping("/faq")
    public List<FAQ> getFaqs() {
        return faqService.getAllFaqs();
    }

    @DeleteMapping("/faq/{id}")
    public String deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
        return "FAQ deleted";
    }
}