package com.kodilla.outdoor.equiprent.filter.logging;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.stream.Collectors;

public class CachedHttpServletRequest extends HttpServletRequestWrapper {
    private byte[] cachedRequest;

    public CachedHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        cachedRequest = StreamUtils.copyToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(cachedRequest);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedRequest);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    public String getCachedRequest() {
        return getReader().lines().collect(Collectors.joining(""));
    }

    @Slf4j
    private static class CachedServletInputStream extends ServletInputStream {
        private InputStream cachedInputStream;

        public CachedServletInputStream(byte[] cachedBody) {
            this.cachedInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            try {
                return cachedInputStream.available() == 0;
            } catch (IOException exp) {
                log.error(exp.getMessage());
            }
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return cachedInputStream.read();
        }
    }
}
