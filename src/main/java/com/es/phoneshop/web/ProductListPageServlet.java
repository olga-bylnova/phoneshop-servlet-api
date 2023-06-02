package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.QuantityNotIntegerException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.es.phoneshop.util.ErrorHandler.handleError;
import static com.es.phoneshop.util.QuantityParser.getQuantity;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private ProductService productService;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        productService = HttpSessionProductService.getInstance();
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        String sortFieldParam = request.getParameter("sort");
        String sortOrderParam = request.getParameter("order");

        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortFieldParam).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrderParam).map(SortOrder::valueOf).orElse(null)
        ));
        request.setAttribute("productReview", productService.getRecentlyReviewedProducts(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = Long.parseLong(request.getParameter("productId"));
        int quantity;

        Map<Long, String> errors = new HashMap<>();
        try {
            quantity = getQuantity(request, request.getParameter("quantity"));

            Cart cart = cartService.getCart(request);
            cartService.add(cart, productId, quantity);
        } catch (ParseException | OutOfStockException | QuantityNotIntegerException e) {
            handleError(errors, productId, e);
        }

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products?message=Product with id " + productId + " was added to cart");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
