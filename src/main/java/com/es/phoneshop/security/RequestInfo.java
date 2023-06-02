package com.es.phoneshop.security;

import java.time.LocalDateTime;

public class RequestInfo {
    private Long requestCount;
    private LocalDateTime requestStartTime;

    public RequestInfo(Long requestCount, LocalDateTime requestStartTime) {
        this.requestCount = requestCount;
        this.requestStartTime = requestStartTime;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public LocalDateTime getRequestStartTime() {
        return requestStartTime;
    }

    public void setRequestStartTime(LocalDateTime requestStartTime) {
        this.requestStartTime = requestStartTime;
    }
}
