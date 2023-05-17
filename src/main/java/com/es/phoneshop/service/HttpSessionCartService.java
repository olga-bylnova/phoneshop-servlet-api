package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public class HttpSessionCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";
    private final static ProductDao productDao = ArrayListProductDao.getInstance();
    private final static CartService INSTANCE = new HttpSessionCartService();

    public static CartService getInstance() {
        return INSTANCE;
    }

    private HttpSessionCartService() {
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        Optional<CartItem> itemOptional = findCartItemForUpdate(cart, productId, quantity);
        int existingProductsAmount = itemOptional.map(CartItem::getQuantity).orElse(0);

        checkStockAvailable(product, existingProductsAmount + quantity);

        if (itemOptional.isPresent()) {
            CartItem item = itemOptional.get();
            quantity += item.getQuantity();

            item.setQuantity(quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        checkStockAvailable(product, quantity);

        Optional<CartItem> itemOptional = findCartItemForUpdate(cart, productId, quantity);

        if (itemOptional.isPresent()) {
            CartItem item = itemOptional.get();

            item.setQuantity(quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
    }

    private void checkStockAvailable(Product product, int quantity) throws OutOfStockException {
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

    private Optional<CartItem> findCartItemForUpdate(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        List<CartItem> items = cart.getItems();
        Optional<CartItem> itemOptional = items.stream()
                .filter(item -> productId.equals(item.getProduct().getId()))
                .findAny();

        return itemOptional;
    }
}
