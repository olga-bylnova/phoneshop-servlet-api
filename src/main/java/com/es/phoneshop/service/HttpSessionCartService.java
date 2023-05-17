package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        recalculateCart(cart);
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
        recalculateCart(cart);
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(item ->
                productId.equals(item.getProduct().getId())
        );
        recalculateCart(cart);
    }

    private void checkStockAvailable(Product product, int quantity) throws OutOfStockException {
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

    private Optional<CartItem> findCartItemForUpdate(Cart cart, Long productId, int quantity) throws OutOfStockException {
        List<CartItem> items = cart.getItems();

        return items.stream()
                .filter(item -> productId.equals(item.getProduct().getId()))
                .findAny();
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems()
                .stream()
                .map(CartItem::getQuantity)
                .mapToInt(q -> q)
                .sum());

        BigDecimal totalCost = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            BigDecimal price = item.getProduct().getPrice();
            int quantity = item.getQuantity();

            totalCost = totalCost.add(price.multiply(BigDecimal.valueOf(quantity)));
        }
        cart.setTotalCost(totalCost);
    }
}
