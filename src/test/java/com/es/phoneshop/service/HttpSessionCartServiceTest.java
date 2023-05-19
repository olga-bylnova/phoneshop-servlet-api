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
    private static final Long productId = 1L;
    private ProductDao productDao;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @Before
    public void setUp() {
        cartService = HttpSessionCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        product = new Product(productId, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), Currency.getInstance("USD"), 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");

        productDao.save(product);
    }

    @Before
    public void fillCart() {
        cart = new Cart();

        cart.getItems().add(new CartItem(product, 2));
    }

    @Test
    public void testGetCart() {
        when(request.getSession()).thenReturn(session);

        assertNotNull(cartService.getCart(request));
    }

    @Test
    public void testAddToCartValidQuantity() throws OutOfStockException {
        Cart cart = new Cart();
        int quantity = 1;

        cartService.add(cart, productId, quantity);

        assertTrue(cart.getItems()
                .stream()
                .anyMatch(item -> product.equals(item.getProduct()) && quantity == item.getQuantity()));
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartQuantityOutOfStockThrowsOutOfStockException() throws OutOfStockException {
        int quantity = 5;

        cartService.add(cart, productId, quantity);
    }

    @Test
    public void testAddToCartProductExistsInCart() throws OutOfStockException {
        int sumQuantity = 3;

        cartService.add(cart, productId, 1);

        assertTrue(cart.getItems()
                .stream()
                .anyMatch(item -> product.equals(item.getProduct()) && sumQuantity == item.getQuantity()));
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartProductExistsInCartQuantityOutOfStockThrowOutOfStockException() throws OutOfStockException {
        cartService.add(cart, productId, 3);
    }

    @Test
    public void testUpdateWhenProductIsInCart() throws OutOfStockException {
        int newQuantity = 3;

        cartService.update(cart, productId, newQuantity);

        assertTrue(cart.getItems()
                .stream()
                .anyMatch(item -> productId.equals(item.getProduct().getId())
                        && newQuantity == item.getQuantity()));
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateWhenProductIsInCartQuantityOutOfStockThrowOutOfStockException() throws OutOfStockException {
        int newQuantity = 4;

        cartService.update(cart, productId, newQuantity);

        assertTrue(cart.getItems()
                .stream()
                .anyMatch(item -> productId.equals(item.getProduct().getId())
                        && newQuantity == item.getQuantity()));
    }

    @Test
    public void testUpdateWhenProductIsNotInCartThenCreateProductInCart() throws OutOfStockException {
        Cart cart = new Cart();
        int newQuantity = 3;

        cartService.update(cart, productId, newQuantity);

        assertTrue(cart.getItems()
                .stream()
                .anyMatch(item -> productId.equals(item.getProduct().getId())
                        && newQuantity == item.getQuantity()));
    }

    @Test
    public void testDelete() {
        cartService.delete(cart, productId);

        assertTrue(cart.getItems()
                .stream()
                .noneMatch(item -> productId.equals(item.getProduct().getId())));
    }
}