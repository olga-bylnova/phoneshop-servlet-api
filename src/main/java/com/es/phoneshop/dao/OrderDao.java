package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order getOrderBySecureId(String id);
    Order getItem(Long id);
    void save(Order order);
}
