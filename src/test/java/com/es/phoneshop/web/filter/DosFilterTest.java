package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.DosProtectionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    private FilterChain filterChain;
    @Mock
    private ServletRequest request;
    @Mock
    private ServletResponse response;
    @Mock
    private DosProtectionService dosProtectionService;
    @InjectMocks
    private DosFilter dosFilter = new DosFilter();

    @Test
    public void doFilterWhenRequestAllowed() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(any())).thenReturn(true);

        dosFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

}