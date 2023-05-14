package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCartServiceTest {

    private CartService cartService;
    private Product product;
    private Cart cart;
    private ProductDao productDao;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @Before
    public void setUp() {
        cartService = HttpSessionCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        product = new Product(1L, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), Currency.getInstance("USD"), 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
        cart = new Cart();
    }

    @Test
    public void testGetCart() {
        when(request.getSession()).thenReturn(session);

        assertNotNull(cartService.getCart(request));
    }

    @Test
    public void testAddToCartValidQuantity() throws OutOfStockException {
        int quantity = 3;
        productDao.save(product);

        cartService.add(cart, 1L, quantity);

        assertTrue(cart.getItems()
                .stream()
                .anyMatch(item -> product.equals(item.getProduct()) && quantity == item.getQuantity()));
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartQuantityOutOfStockThrowsOutOfStockException() throws OutOfStockException {
        int quantity = 5;
        productDao.save(product);

        cartService.add(cart, 1L, quantity);
    }

    @Test
    public void testAddToCartProductExistsInCart() throws OutOfStockException {
        Cart cart = new Cart();
        int sumQuantity = 3;
        CartItem cartItem = new CartItem(product, 2);

        cart.getItems().add(cartItem);
        productDao.save(product);

        cartService.add(cart, 1L, 1);

        assertTrue(cart.getItems()
                .stream()
                .anyMatch(item -> product.equals(item.getProduct()) && sumQuantity == item.getQuantity()));
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartProductExistsInCartQuantityOutOfStockThrowOutOfStockException() throws OutOfStockException {
        Cart cart = new Cart();
        CartItem cartItem = new CartItem(product, 2);

        cart.getItems().add(cartItem);
        productDao.save(product);

        cartService.add(cart, 1L, 3);
    }
}