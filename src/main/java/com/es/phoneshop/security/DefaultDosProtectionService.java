package com.es.phoneshop.security;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final int THRESHOLD = 20;
    private static final int REQUEST_TIME_LIMIT_MIN = 1;
    private Map<String, RequestInfo> countMap = new ConcurrentHashMap<>();
    private static final DosProtectionService INSTANCE = new DefaultDosProtectionService();

    public static DosProtectionService getInstance() {
        return INSTANCE;
    }

    private DefaultDosProtectionService() {
    }

    @Override
    public boolean isAllowed(String ip) {
        RequestInfo requestInfo = countMap.get(ip);
        if (requestInfo == null) {
            requestInfo = new RequestInfo(1L, LocalDateTime.now());
        } else {
            if (requestInfo.getRequestCount() > THRESHOLD) {
                long minutesBetween = ChronoUnit.MINUTES.between(requestInfo.getRequestStartTime(), LocalDateTime.now());
                if (minutesBetween < REQUEST_TIME_LIMIT_MIN) {
                    return false;
                }
                countMap.put(ip, new RequestInfo(1L, LocalDateTime.now()));
                return true;
            }
            requestInfo.setRequestCount(requestInfo.getRequestCount() + 1);
        }
        countMap.put(ip, requestInfo);
        return true;
    }
}
