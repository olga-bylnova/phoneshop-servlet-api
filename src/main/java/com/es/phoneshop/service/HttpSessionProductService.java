package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductReview;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class HttpSessionProductService implements ProductService {
    private static final int RECENT_PRODUCTS_COUNT = 3;
    private static final String RECENT_PRODUCTS_SESSION_ATTRIBUTE = HttpSessionProductService.class.getName() + ".recent";
    private final static ProductDao productDao = ArrayListProductDao.getInstance();
    private final static ProductService INSTANCE = new HttpSessionProductService();

    public static ProductService getInstance() {
        return INSTANCE;
    }

    private HttpSessionProductService() {
    }

    @Override
    public synchronized ProductReview getRecentlyReviewedProducts(HttpServletRequest request) {
        ProductReview recentProducts = (ProductReview) request.getSession().getAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE);
        if (recentProducts == null) {
            recentProducts = new ProductReview(new ArrayList<>());
            request.getSession().setAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE, recentProducts);
        }
        return recentProducts;
    }

    @Override
    public synchronized void updateRecentlyReviewedProducts(List<Product> recentProducts, Long productId, HttpServletRequest request) {
        Product product = productDao.getItem(productId);

        recentProducts.removeIf(product1 -> product1.getId().equals(productId));
        recentProducts.add(0, product);

        if (recentProducts.size() > RECENT_PRODUCTS_COUNT) {
            recentProducts.remove(RECENT_PRODUCTS_COUNT);
        }
    }
}
