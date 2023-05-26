package com.es.phoneshop.exception;

public class EntityNotFoundException extends RuntimeException {
    private String id;

    public EntityNotFoundException(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
