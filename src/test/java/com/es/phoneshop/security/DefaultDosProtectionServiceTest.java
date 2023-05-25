package com.es.phoneshop.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDosProtectionServiceTest {
    private DosProtectionService dosProtectionService = DefaultDosProtectionService.getInstance();

    @Test
    public void isAllowedWhenFirstRequestThenReturnTrue() {
        String ip = "1";

        assertTrue(dosProtectionService.isAllowed(ip));
    }

    @Test
    public void isAllowedWhenTooManyRequestsThenReturnFalse() {
        String ip = "1";
        int requestLimit = 21;

        for (int i = 0; i < requestLimit; i++) {
            dosProtectionService.isAllowed(ip);
        }
        assertFalse(dosProtectionService.isAllowed(ip));
    }
}