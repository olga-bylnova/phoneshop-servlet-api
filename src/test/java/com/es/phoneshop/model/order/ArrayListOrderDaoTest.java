package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.exception.OrderNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;
    private Order order;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();

        order = new Order(new BigDecimal(5), new BigDecimal(5), "test", "test", "test", LocalDate.now(), "test", PaymentMethod.CASH);
    }

    @Test
    public void whenGetOrderById() {
        orderDao.save(order);

        assertNotNull(orderDao.getItem(order.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenNullWhenGetOrderThenThrowOrderNotFoundException() {
        orderDao.getItem(null);
    }

    @Test
    public void givenOrderWithNoIdWhenSaveThenSetOrderId() {
        orderDao.save(order);

        assertNotNull(order.getId());
    }

    @Test
    public void givenOrderWithIdWhenSaveThenSaveOrderWithSameId() {
        Long orderId = 50L;
        order.setId(orderId);
        orderDao.save(order);

        assertEquals(orderId, order.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullWhenSaveOrderThenThrowIllegalArgumentException() {
        orderDao.save(null);
    }

    @Test
    public void givenOrderWithExistingIdWhenSaveOrderThenUpdateOrder() {
        BigDecimal newTotalCost = new BigDecimal(20);

        orderDao.save(order);
        order.setTotalCost(newTotalCost);
        orderDao.save(order);

        assertEquals(newTotalCost, orderDao.getItem(order.getId()).getTotalCost());
    }

    @Test
    public void whenGetOrderBySecureId() {
        String secureId = UUID.randomUUID().toString();
        order.setSecureId(secureId);
        orderDao.save(order);

        assertNotNull(orderDao.getOrderBySecureId(secureId));
    }

    @Test(expected = OrderNotFoundException.class)
    public void givenNullWhenGetOrderBySecureIdThenThrowOrderNotFoundException() {
        orderDao.getOrderBySecureId(null);
    }
}
