package ru.erulaev.restaurantvoting.user.service;


import java.util.List;

public interface FoodService<E, T> {

    List<T> getAll(int parentId);

    T get(int entityId, int parentId);

    T save(E entity, int parentId);

    void delete(int entityId, int parentId);

    default void update(E entity, int entityId, int parentId) {
        throw new UnsupportedOperationException("Update is unsupported");
    }
}