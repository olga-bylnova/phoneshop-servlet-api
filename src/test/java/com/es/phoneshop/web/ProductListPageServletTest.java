package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductReview;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.ProductService;
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
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductService productService;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartService cartService;
    @InjectMocks
    private ProductListPageServlet servlet = new ProductListPageServlet();
    private static final String validProductIdParameter = "11";
    private static final String validQuantityParameter = "11";

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(productService.getRecentlyReviewedProducts(any())).thenReturn(new ProductReview());

        when(request.getParameter("productId")).thenReturn(validProductIdParameter);
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getParameter("quantity")).thenReturn(validQuantityParameter);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/productList.jsp"));
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), any());
        verify(request).setAttribute(eq("productReview"), any());
    }

    @Test
    public void testDoPostWhenValidParameters() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
    }

    @Test
    public void testDoPostWhenQuantityNotANumber() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("asf");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPostWhenQuantityOutOfStock() throws ServletException, IOException, OutOfStockException {
        doThrow(new OutOfStockException(new Product(), 0, 0)).when(cartService).add(any(), anyLong(), anyInt());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }
}