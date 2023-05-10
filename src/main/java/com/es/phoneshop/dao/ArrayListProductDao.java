package com.es.phoneshop.dao;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static final ProductDao INSTANCE = new ArrayListProductDao();

    public static ProductDao getInstance() {
        return INSTANCE;
    }

    private static final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private List<Product> products;
    private long id;

    private ArrayListProductDao() {
        products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) {
        try {
            lock.readLock().lock();
            return products.stream()
                    .filter(product -> id != null && id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        try {
            lock.readLock().lock();

            List<String> searchKeywords = splitQuery(query);

            Predicate<Product> matchesSearchCriteria = (product) -> searchKeywords
                    .stream()
                    .anyMatch(keyword -> product.getDescription()
                            .toLowerCase()
                            .contains(keyword));

            return products.stream()
                    .filter(product -> searchKeywords.isEmpty() || matchesSearchCriteria.test(product))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(sortField != null ? getSortingComparatorByFieldAndOrder(sortField, sortOrder) :
                            getSearchKeywordComparator(searchKeywords))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    private List<String> splitQuery(String query) {
        List<String> splitQuery = new ArrayList<>();
        if (query != null) {
            splitQuery = Arrays.stream(query
                            .toLowerCase()
                            .split("\\s+"))
                    .toList();
        }
        return splitQuery;
    }

    private Comparator<Product> getSortingComparatorByFieldAndOrder(SortField sortField,
                                                                    SortOrder sortOrder) {
        Comparator<Product> comparator = Comparator.comparing(product -> {
                    if (SortField.description == sortField) {
                        return (Comparable) product.getDescription();
                    } else {
                        return (Comparable) product.getPrice();
                    }
                }
        );
        if (SortOrder.desc == sortOrder) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private Comparator<Product> getSearchKeywordComparator(List<String> searchKeywords) {
        Comparator<Product> comparator = (p1, p2) -> 0;
        if (!searchKeywords.isEmpty()) {
            comparator = Comparator.<Product, Long>comparing(product ->
                            searchKeywords.stream()
                                    .filter(keyword -> Arrays.asList(product.getDescription()
                                            .toLowerCase()
                                            .split(" ")).contains(keyword))
                                    .count(), Comparator.reverseOrder())
                    .thenComparing(product -> {
                                long matchingWordCount = searchKeywords
                                        .stream()
                                        .filter(keyword -> product.getDescription()
                                                .toLowerCase()
                                                .contains(keyword))
                                        .count();
                                long descriptionLength = product.getDescription().split(" ").length;
                                return (descriptionLength - matchingWordCount);
                            }
                    );
        }
        return comparator;
    }

    @Override
    public void save(Product product) {
        try {
            lock.writeLock().lock();

            if (product == null) {
                throw new IllegalArgumentException();
            }

            if (product.getId() == null) {
                product.setId(id++);
            } else {
                delete(product.getId());
            }
            products.add(product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            lock.writeLock().lock();
            products.removeIf(product -> id != null && id.equals(product.getId()));
        } finally {
            lock.writeLock().unlock();
        }
    }
}
