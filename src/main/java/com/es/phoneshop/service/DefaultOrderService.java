package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private final OrderDao orderDao = ArrayListOrderDao.getInstance();
    private final static OrderService INSTANCE = new DefaultOrderService();

    public static OrderService getInstance() {
        return INSTANCE;
    }

    private DefaultOrderService() {
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems()
                .stream()
                .map(item -> {
                    try {
                        return (CartItem) item.clone();
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList()));
        order.setSubTotal(cart.getTotalCost());
        order.setDeliveryCost(calculateTotalCost());
        order.setTotalCost(order.getSubTotal().add(order.getDeliveryCost()));

        return order;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.stream(PaymentMethod.values()).toList();
    }

    private BigDecimal calculateTotalCost() {
        return new BigDecimal(5);
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }
}
