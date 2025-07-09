package ru.erulaev.restaurantvoting.user.service;


import java.util.List;
import java.util.Optional;

public interface FoodService<Entity, To> {

    List<To> getAll(long parentId);

    To get(long entityId, long parentId);

    To save(Entity entity, long parentId);

    void delete(long entityId, long parentId);

    default void update(Entity entity, long entityId, long parentId) {
        throw new UnsupportedOperationException("Update is unsupported");
    }
}