package com.es.phoneshop.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    private OrderService orderService = DefaultOrderService.getInstance();
    private Order order;
    private Product product;

    @Before
    public void setUp() {
        order = new Order(new BigDecimal(5), new BigDecimal(5), "test", "test", "test", LocalDate.now(), "test", PaymentMethod.CASH);
        product = new Product(1L, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), Currency.getInstance("USD"), 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
    }

    @Test
    public void getOrderReturnOrderWithCartParameters() {
        Cart cart = new Cart();
        cart.getItems().add(new CartItem(product, 1));
        cart.setTotalCost(new BigDecimal(320));

        Order orderFromCart = orderService.getOrder(cart);

        assertNotNull(orderFromCart.getSubTotal());
        assertNotNull(orderFromCart.getTotalCost());
        assertNotNull(orderFromCart.getDeliveryCost());

        assertFalse(orderFromCart.getItems().isEmpty());
    }

    @Test
    public void getPaymentMethodsReturnNotEmptyList() {
        List<PaymentMethod> paymentMethods = orderService.getPaymentMethods();

        assertFalse(paymentMethods.isEmpty());
    }

    @Test
    public void placeOrderSetOrderWithIdAndSecureId() {
        orderService.placeOrder(order);

        assertNotNull(order.getId());
        assertNotNull(order.getSecureId());
    }
}