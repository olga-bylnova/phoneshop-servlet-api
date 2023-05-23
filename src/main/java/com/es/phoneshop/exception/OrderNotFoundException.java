package com.es.phoneshop.exception;

public class OrderNotFoundException extends RuntimeException {
    private Long id;

    public OrderNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
