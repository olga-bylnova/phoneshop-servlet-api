package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
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
        productDao = ArrayListProductDao.getInstance();
        getSampleProducts();

        usd = Currency.getInstance("USD");
        product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(10), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
    }

    private void getSampleProducts() {
        Currency usd = Currency.getInstance("USD");

        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }

    @Test
    public void whenFindProductsThenReturnListNotEmpty() {
        assertFalse(productDao.findProducts(null, null, null).isEmpty());
    }

    @Test
    public void whenFindProductsThenReturnProductsWithNotNullPrice() {
        Product productWithNullPrice = new Product("sgs", "Samsung Galaxy S", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productWithNullPrice);

        assertTrue(productDao.findProducts(null, null, null)
                .stream()
                .allMatch(product -> product.getPrice() != null));
    }

    @Test
    public void whenFindProductsThenReturnProductsWithPositiveStock() {
        Product productWithZeroStock = new Product("sgs", "Samsung Galaxy S", new BigDecimal(2), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productWithZeroStock);

        assertTrue(productDao.findProducts(null, null, null)
                .stream()
                .allMatch(product -> product.getStock() > 0));
    }

    @Test
    public void whenGetProduct() {
        productDao.save(product);

        assertNotNull(productDao.getProduct(product.getId()));
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNullWhenGetProductThenThrowProductNotFoundException() {
        productDao.getProduct(null);
    }

    @Test
    public void givenProductWithNoIdWhenSaveThenSaveProductWithId() {
        productDao.save(product);

        assertNotNull(product.getId());
    }

    @Test
    public void givenProductWithIdWhenSaveThenSaveProductWithSameId() {
        Product productWithId = new Product(222L, "sgs", "Samsung Galaxy S", new BigDecimal(2), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productWithId);

        assertEquals(222L, (long) productWithId.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullWhenSaveProductThenThrowIllegalArgumentException() {
        productDao.save(null);
    }

    @Test
    public void givenProductWithExistingIdWhenSaveProductThenUpdateProduct() {
        String newDescription = "Test Phone";

        productDao.save(product);
        product.setDescription(newDescription);
        productDao.save(product);

        assertEquals(newDescription, productDao.getProduct(product.getId()).getDescription());
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenIdWhenDeleteThenDeleteProduct() {
        productDao.save(product);

        productDao.delete(product.getId());

        productDao.getProduct(product.getId());
    }

    @Test
    public void givenNullWhenDeleteThenDeleteNoElements() {
        List<Product> products = productDao.findProducts(null, null, null);

        productDao.delete(null);

        assertEquals(products, productDao.findProducts(null, null, null));
    }

    @Test
    public void givenSearchQueryReturnProductListSortedByQueryMatching() {
        productDao.save(new Product("simc61", "Test phone", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        productDao.save(new Product("sgs3", "Test phone 1 III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        productDao.save(new Product("simsxg75", "Phone 1", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
        productDao.save(new Product("sgs", "Test phone 1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));

        List<Product> expectedSearchResult = List.of(
                new Product("sgs", "Test phone 1", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"),
                new Product("sgs3", "Test phone 1 III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"),
                new Product("simc61", "Test phone", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"),
                new Product("simsxg75", "Phone 1", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        List<Product> actualSearchResult = productDao.findProducts("Test 1", null, null);

        assertEquals(expectedSearchResult, actualSearchResult);
    }

    @Test
    public void givenPriceAndAscReturnProductListSorted() {
        List<Product> searchResult = productDao.findProducts(null, SortField.price, SortOrder.asc);

        assertTrue(isListSortedByPriceAsc(searchResult));
    }

    @Test
    public void givenDescriptionAndAscReturnProductListSorted() {
        List<Product> searchResult = productDao.findProducts(null, SortField.description, SortOrder.asc);

        assertTrue(isListSortedByDescriptionAsc(searchResult));
    }

    @Test
    public void givenPriceAndDescReturnProductListSorted() {
        List<Product> searchResult = productDao.findProducts(null, SortField.price, SortOrder.desc);

        assertTrue(isListSortedByPriceDesc(searchResult));
    }

    @Test
    public void givenDescriptionAndDescReturnProductListSorted() {
        List<Product> searchResult = productDao.findProducts(null, SortField.description, SortOrder.desc);

        assertTrue(isListSortedByDescriptionDesc(searchResult));
    }

    private boolean isListSortedByPriceAsc(List<Product> products) {
        for (int i = 0; i < products.size() - 1; i++) {
            if (products.get(i).getPrice()
                    .compareTo(products.get(i + 1).getPrice()) > 0)
                return false;
        }
        return true;
    }

    private boolean isListSortedByDescriptionAsc(List<Product> products) {
        for (int i = 0; i < products.size() - 1; i++) {
            if (products.get(i).getDescription().
                    compareTo(products.get(i + 1).getDescription()) > 0)
                return false;
        }
        return true;
    }

    private boolean isListSortedByPriceDesc(List<Product> products) {
        for (int i = 0; i < products.size() - 1; i++) {
            if (products.get(i).getPrice()
                    .compareTo(products.get(i + 1).getPrice()) < 0)
                return false;
        }
        return true;
    }

    private boolean isListSortedByDescriptionDesc(List<Product> products) {
        for (int i = 0; i < products.size() - 1; i++) {
            if (products.get(i).getDescription().
                    compareTo(products.get(i + 1).getDescription()) < 0)
                return false;
        }
        return true;
    }
}
