package com.kodilla.outdoor.equiprent.open.weather.map.interceptor.logging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    public final Logger logger;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logClientHttpRequest(request, body);

        ClientHttpResponse response = execution.execute(request, body);

        logClientHttpResponse(response);

        return response;
    }

    private void logClientHttpRequest(HttpRequest request, byte[] body) {
        // Logging: Request Method, URI
        logger.info("[REQUEST] METHOD: {}, URI: {}", request.getMethod(), request.getURI());
        // Logging: Headers (if exist)
        request.getHeaders().forEach((headerName, headerValues) -> {
            headerValues.forEach(value -> {
                logger.info("[REQUEST] HEADER: {}={}", headerName, value);
            });
        });
//        // Body (if exists)
//        if (body.length > 0) {
//            LOGGER.info("[REQUEST] REQUEST BODY: {}", new String(body));
//        }
    }

    private void logClientHttpResponse(ClientHttpResponse response) throws IOException {
        // Logging: Response Status
        logger.info("[RESPONSE] RESPONSE STATUS: {}", response.getStatusCode());
        // Logging: Response Headers (if exist)
        response.getHeaders().forEach((headerName, headerValues) -> {
            headerValues.forEach(value -> {
                logger.info("[RESPONSE] HEADER: {}={}", headerName, value);
            });
        });
        // Logging: Response Body (if exists)
        String responseBody = new BufferedReader(new InputStreamReader(response.getBody()))
                .lines().collect(Collectors.joining("\n"));
        logger.info("[RESPONSE] RESPONSE BODY: {}", responseBody);
    }
}
