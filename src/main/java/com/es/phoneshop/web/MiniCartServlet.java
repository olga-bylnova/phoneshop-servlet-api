package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.HttpSessionCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MiniCartServlet extends HttpServlet {
    private static final String MINICART_JSP = "/WEB-INF/pages/minicart.jsp";
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));

        request.getRequestDispatcher(MINICART_JSP).include(request, response);
    }
}
