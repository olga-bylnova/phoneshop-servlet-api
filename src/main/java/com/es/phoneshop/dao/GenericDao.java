package com.es.phoneshop.dao;

import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.generic.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GenericDao<T extends Entity> {
    protected static final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    protected List<T> items;
    protected long id;

    public GenericDao() {
        items = new ArrayList<>();
    }

    public T getItem(Long id) {
        try {
            lock.readLock().lock();
            return items.stream()
                    .filter(item -> id != null && id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(() -> new EntityNotFoundException(id != null ? id.toString() : null));
        } finally {
            lock.readLock().unlock();
        }
    }

    public void save(T item) {
        try {
            lock.writeLock().lock();

            if (item == null) {
                throw new IllegalArgumentException();
            }

            if (item.getId() == null) {
                item.setId(id++);
            } else {
                items.removeIf(item1 -> item.getId().equals(item1.getId()));
            }
            items.add(item);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
