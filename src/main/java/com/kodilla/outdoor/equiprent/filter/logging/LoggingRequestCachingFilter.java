package com.kodilla.outdoor.equiprent.filter.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Component
@Order(1)
@WebFilter(filterName = "LoggingRequestCachingFilter", urlPatterns = "/*")
public class LoggingRequestCachingFilter extends OncePerRequestFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingRequestCachingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        CachedHttpServletResponse cachedHttpServletResponse = new CachedHttpServletResponse(response);

        logCatchingRequest(cachedHttpServletRequest);

        filterChain.doFilter(cachedHttpServletRequest, cachedHttpServletResponse);

        logCatchingResponse(cachedHttpServletResponse);

        // Sending Response to Http Client
        byte[] responseContent = cachedHttpServletResponse.getContentAsByteArray();
        response.getOutputStream().write(responseContent);  // Saving the response to OutputStream
        response.getOutputStream().flush();  // Flushing to ensure the content is sent
    }

    private void logCatchingRequest(CachedHttpServletRequest cachedHttpServletRequest) {
        // Logging: Request Method, URI
        LOGGER.info("[REQUEST] METHOD: {}, URI: {}", cachedHttpServletRequest.getMethod(), cachedHttpServletRequest.getRequestURI());
        // Logging: Headers (if exist)
        Enumeration<String> headerNames = cachedHttpServletRequest.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                LOGGER.info("[REQUEST] HEADER: {}={}", headerName, cachedHttpServletRequest.getHeader(headerName));
            }
        }
        // Logging: Request body (if exists)
        String requestBody = cachedHttpServletRequest.getCachedRequest();
        if (!requestBody.isEmpty()) {
            LOGGER.info("[REQUEST] REQUEST BODY: {}", requestBody);
        }
    }

    private void logCatchingResponse(CachedHttpServletResponse cachedHttpServletResponse) throws IOException {
        // Logging: Response Status
        LOGGER.info("[RESPONSE] RESPONSE STATUS: {}", cachedHttpServletResponse.getStatus());
        // Logging: Response Headers (if exist)
        cachedHttpServletResponse.getHeaderNames().stream().forEach(e -> {
            LOGGER.info("[RESPONSE] HEADER: {}={}", e, cachedHttpServletResponse.getHeader(e));
        });
        // Logging: Response Body (if exists)
        byte[] responseContent = cachedHttpServletResponse.getContentAsByteArray();
        if (responseContent.length > 0) {
            LOGGER.info("[RESPONSE] RESPONSE BODY: {}", new String(responseContent, StandardCharsets.UTF_8));
        }
    }
}