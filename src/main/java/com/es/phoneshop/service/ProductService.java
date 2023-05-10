package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductReview;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ProductService {
    ProductReview getRecentlyReviewedProducts(HttpServletRequest request);
    void updateRecentlyReviewedProducts(List<Product> recentProducts, Long productId, HttpServletRequest request);
}
