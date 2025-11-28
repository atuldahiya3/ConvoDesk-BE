package com.convodesk.backend.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {   // ← changed to OncePerRequestFilter

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. COMPLETELY SKIP tenant check for all /auth/** endpoints
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Allow SUPER_ADMIN without any tenant check
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> "ROLE_SUPER_ADMIN".equals(role))) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. For protected endpoints (you already check /business, keep it or broaden)
        if (path.startsWith("/business") || path.startsWith("/api")) {  // add any other protected path

            Long tenantId = TenantContext.getTenantId();
            if (tenantId == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Tenant ID missing in token\"}");
                return;
            }

            // OPTIONAL: extra safety — verify the user actually belongs to this tenant
            // (you can skip if you trust the JWT)
        }

        filterChain.doFilter(request, response);
    }
}