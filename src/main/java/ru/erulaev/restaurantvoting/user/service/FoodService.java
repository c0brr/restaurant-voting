package ru.erulaev.restaurantvoting.user.service;


import java.util.List;

public interface FoodService<E, T> {

    List<T> getAll(long parentId);

    T get(long entityId, long parentId);

    T save(E entity, long parentId);

    void delete(long entityId, long parentId);

    default void update(E entity, long entityId, long parentId) {
        throw new UnsupportedOperationException("Update is unsupported");
    }
}