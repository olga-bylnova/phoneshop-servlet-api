package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private static final OrderDao INSTANCE = new ArrayListOrderDao();

    public static OrderDao getInstance() {
        return INSTANCE;
    }

    private static final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private List<Order> orders;
    private long id;

    private ArrayListOrderDao() {
        orders = new ArrayList<>();
    }

    @Override
    public Order getOrder(Long id) {
        try {
            lock.readLock().lock();
            return orders.stream()
                    .filter(order -> id != null && id.equals(order.getId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Order order) {
        try {
            lock.writeLock().lock();

            if (order == null) {
                throw new IllegalArgumentException();
            }

            if (order.getId() == null) {
                order.setId(id++);
            } else {
                orders.removeIf(order1 -> order.getId().equals(order1.getId()));
            }
            orders.add(order);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
