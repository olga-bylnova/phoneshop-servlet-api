package com.es.phoneshop.service;

import com.es.phoneshop.model.product.ProductReview;
import jakarta.servlet.http.HttpServletRequest;

public interface ProductService {
    ProductReview getRecentlyReviewedProducts(HttpServletRequest request);
    void updateRecentlyReviewedProducts(Long productId, HttpServletRequest request);
}
