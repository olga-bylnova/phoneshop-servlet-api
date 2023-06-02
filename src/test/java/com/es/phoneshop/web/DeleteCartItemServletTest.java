package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
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
public class DeleteCartItemServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private CartService cartService;
    @InjectMocks
    private DeleteCartItemServlet servlet = new DeleteCartItemServlet();
    private static final String validProductId = "\\11";

    @Before
    public void setUp() {
        when(request.getPathInfo()).thenReturn(validProductId);
    }

    @Test
    public void doPost() throws IOException, ServletException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
    }
}