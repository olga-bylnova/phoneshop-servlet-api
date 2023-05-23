package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.List;

public interface OrderDao {
    Order getOrder(Long id);
    void save(Order order);
}
