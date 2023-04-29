package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private Product product;
    private Currency usd;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();

        usd = Currency.getInstance("USD");
        product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(10), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
    }

    @Test
    public void whenFindProducts_thenReturnListNotEmpty() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void whenFindProducts_thenReturnProductsWithNotNullPrice() {
        Product productWithNullPrice = new Product("sgs", "Samsung Galaxy S", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productWithNullPrice);

        assertTrue(productDao.findProducts()
                .stream()
                .allMatch(product -> product.getPrice() != null));
    }

    @Test
    public void whenFindProducts_thenReturnProductsWithPositiveStock() {
        Product productWithZeroStock = new Product("sgs", "Samsung Galaxy S", new BigDecimal(2), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productWithZeroStock);

        assertTrue(productDao.findProducts()
                .stream()
                .allMatch(product -> product.getStock() > 0));
    }

    @Test
    public void whenGetProduct() {
        productDao.save(product);

        assertNotNull(productDao.getProduct(product.getId()));
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNull_whenGetProduct_thenThrowProductNotFoundException() {
        productDao.getProduct(null);
    }

    @Test
    public void givenProductWithNoId_whenSave_thenSaveProductWithId() {
        productDao.save(product);

        assertNotNull(product.getId());
    }

    @Test
    public void givenProductWithId_whenSave_thenSaveProductWithSameId() {
        Product productWithId = new Product(222L, "sgs", "Samsung Galaxy S", new BigDecimal(2), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productWithId);

        assertEquals(222L, (long) productWithId.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNull_whenSaveProduct_thenThrowIllegalArgumentException() {
        productDao.save(null);
    }

    @Test
    public void givenProductWithExistingId_whenSaveProduct_thenUpdateProduct() {
        String newDescription = "Test Phone";

        productDao.save(product);
        product.setDescription(newDescription);
        productDao.save(product);

        assertEquals(newDescription, productDao.getProduct(product.getId()).getDescription());
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenId_whenDelete_thenDeleteProduct() {
        productDao.save(product);

        productDao.delete(product.getId());

        productDao.getProduct(product.getId());
    }

    @Test
    public void givenNull_whenDelete_thenDeleteNoElements() {
        List<Product> products = productDao.findProducts();

        productDao.delete(null);

        assertEquals(products, productDao.findProducts());
    }
}
