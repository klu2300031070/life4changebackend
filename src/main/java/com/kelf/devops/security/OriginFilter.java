package com.kelf.devops.security;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OriginFilter implements Filter {

    private static final String ALLOWED_ORIGIN = "https://life4change.onrender.com/";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String origin = req.getHeader("Origin");

        if (origin != null && origin.equals(ALLOWED_ORIGIN)) {
            chain.doFilter(request, response);
            return;
        }

        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
