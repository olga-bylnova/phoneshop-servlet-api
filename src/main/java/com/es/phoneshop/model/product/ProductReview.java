package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.util.List;

public class ProductReview {
    List<Product> recentProducts;

    public ProductReview() {
    }

    public List<Product> getRecentProducts() {
        return recentProducts;
    }

    public ProductReview(List<Product> recentProducts) {
        this.recentProducts = recentProducts;
    }
}
