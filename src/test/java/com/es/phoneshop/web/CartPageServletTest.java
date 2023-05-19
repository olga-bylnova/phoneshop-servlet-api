package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import jakarta.servlet.RequestDispatcher;
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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @InjectMocks
    private CartPageServlet servlet = new CartPageServlet();
    private static final String[] validProductIds = new String[]{"1"};
    private static final String[] validQuantities = new String[]{"1"};
    private static final String[] invalidQuantities = new String[]{"asdf"};
    private static final String CART_JSP = "/WEB-INF/pages/cart.jsp";

    @Before
    public void init() {
        when(request.getParameterValues("productId")).thenReturn(validProductIds);
        when(request.getParameterValues("quantity")).thenReturn(validQuantities);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.US);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cart"), any());
        verify(request).getRequestDispatcher(eq(CART_JSP));
    }

    @Test
    public void testDoPostWhenValidParameters() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
    }

    @Test
    public void testDoPostWhenQuantityNotANumber() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(invalidQuantities);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostWhenProductOutOfStock() throws ServletException, IOException, OutOfStockException {
        doThrow(new OutOfStockException(new Product(), 0, 0)).when(cartService).update(any(), anyLong(), anyInt());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }
}