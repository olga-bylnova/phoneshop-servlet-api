package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.HttpSessionCartService;
import com.es.phoneshop.service.HttpSessionProductService;
import com.es.phoneshop.service.ProductService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class DeleteCartItemServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        Cart cart = cartService.getCart(request);

        cartService.delete(cart, productId);

        response.sendRedirect(request.getContextPath() + "/cart?message=Product with id " + productId + " was removed from cart");
    }
    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo();
        return Long.parseLong(productId.substring(1));
    }
}
