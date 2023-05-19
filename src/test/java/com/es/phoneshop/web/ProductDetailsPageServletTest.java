package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
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
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartService cartService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    private static final String validProductId = "\\11";
    public static final String PRODUCT_JSP = "/WEB-INF/pages/product.jsp";

    @Before
    public void init() {
        when(request.getPathInfo()).thenReturn(validProductId);

        when(cartService.getCart(any())).thenReturn(new Cart());
        when(productService.getRecentlyReviewedProducts(any())).thenReturn(new ProductReview());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.US);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(validProductId);
        when(productDao.getProduct(any())).thenReturn(new Product());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), any());
        verify(request).setAttribute(eq("cart"), any());
        verify(request).setAttribute(eq("productReview"), any());
        verify(request).getRequestDispatcher(eq(PRODUCT_JSP));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWhenValidParameters() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("1");

        servlet.doPost(request, response);

        verify(request).getParameter("quantity");
    }

    @Test
    public void testDoPostWhenQuantityNotANumber() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("sdf");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), any());
    }

    @Test
    public void testDoPostWhenProductOutOfStock() throws ServletException, IOException, OutOfStockException {
        when(request.getParameter("quantity")).thenReturn("1");
        doThrow(new OutOfStockException(new Product(), 0, 0)).when(cartService).add(any(), anyLong(), anyInt());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), any());
    }
}