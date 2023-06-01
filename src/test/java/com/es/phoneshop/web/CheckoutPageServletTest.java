package com.es.phoneshop.web;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private OrderService orderService;
    @Mock
    private CartService cartService;
    @InjectMocks
    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    private static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";

    @Before
    public void init() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(orderService.getOrder(any())).thenReturn(new Order());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());

        verify(request).getRequestDispatcher(eq(CHECKOUT_JSP));
    }

    @Test
    public void testDoPostWhenValidParameters() throws ServletException, IOException {
        String testString = "test";
        String testPhoneString = "+1";
        String testDateString = "2020-01-01";
        String testPaymentMethodString = "CASH";

        when(request.getParameter("firstName")).thenReturn(testString);
        when(request.getParameter("lastName")).thenReturn(testString);
        when(request.getParameter("phone")).thenReturn(testPhoneString);
        when(request.getParameter("deliveryAddress")).thenReturn(testString);
        when(request.getParameter("deliveryDate")).thenReturn(testDateString);
        when(request.getParameter("paymentMethod")).thenReturn(testPaymentMethodString);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("order"), any());

        verify(response).sendRedirect(any());
    }

    @Test
    public void testDoPostWhenInvalidParameters() throws ServletException, IOException {
        String testString = "";
        String testDateString = null;
        String testPaymentMethodString = null;

        when(request.getParameter("firstName")).thenReturn(testString);
        when(request.getParameter("lastName")).thenReturn(testString);
        when(request.getParameter("phone")).thenReturn(testString);
        when(request.getParameter("deliveryAddress")).thenReturn(testString);
        when(request.getParameter("deliveryDate")).thenReturn(testDateString);
        when(request.getParameter("paymentMethod")).thenReturn(testPaymentMethodString);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());

        verify(request).getRequestDispatcher(eq(CHECKOUT_JSP));
    }
}