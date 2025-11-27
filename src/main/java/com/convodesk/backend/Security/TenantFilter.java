package com.convodesk.backend.Security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String path = req.getRequestURI();

        // Only check for /business endpoints
        if (path.startsWith("/business")) {

            Long tenantId = TenantContext.getTenantId();

            if (tenantId == null) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Tenant ID missing in token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
