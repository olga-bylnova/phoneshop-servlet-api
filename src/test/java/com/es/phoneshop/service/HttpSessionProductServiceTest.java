package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductReview;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionProductServiceTest {
    private Product product;
    private ProductService productService;
    private ProductDao productDao;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @Before
    public void setUp() {
        productService = HttpSessionProductService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        product = new Product(1L, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), Currency.getInstance("USD"), 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetRecentlyReviewedProducts() {
        assertNotNull(productService.getRecentlyReviewedProducts(request));
    }

    @Test
    public void testUpdateRecentlyReviewedProducts() {
        ProductReview productReview = productService.getRecentlyReviewedProducts(request);
        List<Product> recentProducts = productReview.getRecentProducts();
        productDao.save(product);

        productService.updateRecentlyReviewedProducts(recentProducts, 1L, request);

        assertEquals(product, recentProducts.get(0));
    }

    @Test
    public void testUpdateRecentlyReviewedProductsWithSameProduct() {
        ProductReview productReview = productService.getRecentlyReviewedProducts(request);
        List<Product> recentProducts = productReview.getRecentProducts();
        productDao.save(product);

        productService.updateRecentlyReviewedProducts(recentProducts, 1L, request);
        productService.updateRecentlyReviewedProducts(recentProducts, 1L, request);

        assertEquals(1, recentProducts.size());
    }
}