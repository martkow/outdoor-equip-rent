package com.kodilla.outdoor.equiprent.external.interceptor.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@ExtendWith(MockitoExtension.class)
public class LoggingClientHttpRequestInterceptorTests {
    @InjectMocks
    private LoggingClientHttpRequestInterceptor interceptor;
    @Mock
    private Logger logger;
    @Mock
    private HttpRequest httpRequest;
    @Mock
    private ClientHttpRequestExecution execution;
    @Mock
    private ClientHttpResponse httpResponse;

    @Test
    void shouldLogRequestAndResponse() throws IOException {
        // Given
        Mockito.when(httpRequest.getMethod()).thenReturn(HttpMethod.GET);
        Mockito.when(httpRequest.getURI()).thenReturn(URI.create(("http://test.com")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("headerName", "headerValue");
        Mockito.when(httpRequest.getHeaders()).thenReturn(httpHeaders);

        Mockito.when(httpResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        HttpHeaders httpResponseHeaders = new HttpHeaders();
        httpResponseHeaders.add("responseHeaderName", "responseHeaderValue");
        Mockito.when(httpResponse.getHeaders()).thenReturn(httpResponseHeaders);

        String responseBody = "Response Body";
        Mockito.when(httpResponse.getBody()).thenReturn(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8)));
        // When
        Mockito.when(execution.execute(Mockito.any(), Mockito.any())).thenReturn(httpResponse);
        interceptor.intercept(httpRequest, new byte[0], execution);
        // Then
        Mockito.verify(logger).info("[REQUEST] METHOD: {}, URI: {}", HttpMethod.GET, URI.create(("http://test.com")));
        Mockito.verify(logger).info("[REQUEST] HEADER: {}={}", "headerName", "headerValue");
        Mockito.verify(logger).info("[RESPONSE] RESPONSE STATUS: {}", HttpStatus.OK);
        Mockito.verify(logger).info("[RESPONSE] HEADER: {}={}", "responseHeaderName", "responseHeaderValue");
        Mockito.verify(logger).info("[RESPONSE] RESPONSE BODY: {}", "Response Body");
    }
}
