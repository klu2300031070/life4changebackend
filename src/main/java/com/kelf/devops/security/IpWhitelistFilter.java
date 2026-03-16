package com.kelf.devops.security;



import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class IpWhitelistFilter implements Filter {

    // Replace with your laptop public IP or local IP for testing
    private static final Set<String> ALLOWED_IPS = Set.of(
        "192.168.0.4",      // local LAN IP
        "2406:7400:35:aa37:3516:2dfd:af3b:5765"    // public IP from ifconfig.me
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String clientIp = request.getRemoteAddr();
        if (!ALLOWED_IPS.contains(clientIp)) {
            ((HttpServletResponse) response).sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access Denied for IP: " + clientIp
            );
            return;
        }

        chain.doFilter(request, response);
    }
}