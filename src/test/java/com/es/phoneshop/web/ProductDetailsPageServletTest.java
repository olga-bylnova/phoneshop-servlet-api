package com.es.phoneshop.web;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Test
    public void testDoGet() throws ServletException, IOException {
        String productId = "\\11";

        when(request.getPathInfo()).thenReturn(productId);
        when(productDao.getProduct(any())).thenReturn(new Product());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), any());
        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/product.jsp"));
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenProductIdWhenDoGetThrowProductNotFoundException() throws ServletException, IOException {
        String productId = "\\11";

        servlet.init(config);
        when(request.getPathInfo()).thenReturn(productId);

        servlet.doGet(request, response);
    }
}