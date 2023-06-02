package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao {
    private static final OrderDao INSTANCE = new ArrayListOrderDao();

    public static OrderDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Order getOrderBySecureId(String id) {
        try {
            lock.readLock().lock();
            return items.stream()
                    .filter(order -> id != null && id.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }
}
