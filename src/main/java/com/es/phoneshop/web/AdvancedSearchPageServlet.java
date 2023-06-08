package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SearchCriteria;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private static final String ADVANCED_SEARCH_JSP = "/WEB-INF/pages/advancedSearch.jsp";
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("searchCriteria", SearchCriteria.values());

        request.getRequestDispatcher(ADVANCED_SEARCH_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();

        BigDecimal minPrice = parsePrice(request, "minPrice", errors);
        BigDecimal maxPrice = parsePrice(request, "maxPrice", errors);

        String description = request.getParameter("description");
        String searchCriteria = request.getParameter("searchCriteria");

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
        } else {
            List<Product> products = productDao.findProductsByParameters(description, minPrice,
                    maxPrice, SearchCriteria.valueOf(searchCriteria));
            request.setAttribute("products", products);
        }
        doGet(request, response);
    }

    private BigDecimal parsePrice(HttpServletRequest request,
                                  String paramName,
                                  Map<String, String> errors) {
        String paramValue = request.getParameter(paramName);
        BigDecimal value = null;
        try {
            if (paramValue != null && !paramValue.isEmpty()) {
                value = new BigDecimal(paramValue);
            }
        } catch (NumberFormatException e) {
            errors.put(paramName, "Not a number");
        }
        return value;
    }
}