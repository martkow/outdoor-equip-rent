package com.kodilla.outdoor.equiprent.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Wrap request and response with ContentCaching wrappers
        ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(response);

        // Forward the wrapped request and response to be processed by the controller
        request.setAttribute("cachingRequest", cachingRequest);
        request.setAttribute("cachingResponse", cachingResponse);

        // Logging: Request Method, URI, Params
        String params = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        LOGGER.info("REQUEST: Method={}, URI={}{}", request.getMethod(), request.getRequestURI(), params);

        // Logging: Headers (if exist)
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                LOGGER.info("REQUEST HEADER: {}={}", headerName, request.getHeader(headerName));
            }
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Read already saved cachingRequest and catchingResponse
        ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request.getAttribute("cachingRequest");
        ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) request.getAttribute("cachingResponse");

        System.out.println(cachingRequest.getContentAsString());
        // Read request body
        if (cachingRequest != null) {
            String requestBody =  cachingRequest.getContentAsString();
            if (!requestBody.isEmpty()) {
                LOGGER.info("REQUEST BODY: {}", requestBody);
            }
        }

        // Logging: Response Status
        LOGGER.info("RESPONSE: Status={}", response.getStatus());

        // Logging: Headers (if exist)
        response.getHeaderNames().forEach(headerName ->
                LOGGER.info("RESPONSE HEADER: {}={}", headerName, response.getHeader(headerName)));

        // Logging: response body
        if (cachingResponse != null) {
            String responseBody = getCachedResponseBody(cachingResponse);
            if (!responseBody.isEmpty()) {
                LOGGER.info("RESPONSE BODY: {}", responseBody);
            }

            // Copy the response body back to the response stream
            cachingResponse.copyBodyToResponse();
        }
    }

    // Helper method to read the cached response body
    private String getCachedResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length > 0) {
            try {
                return new String(buf, 0, buf.length, response.getCharacterEncoding());
            } catch (UnsupportedEncodingException ex) {
                LOGGER.error("Error reading response body", ex);
            }
        }
        return "";
    }
}
