package ru.erulaev.restaurantvoting.user.service;


import java.util.List;
import java.util.Optional;

public interface FoodService<Entity, EntityTo> {

    List<EntityTo> getAll(long parentId);

    EntityTo get(long entityId, long parentId);

    EntityTo save(Entity entity, long parentId);

    void delete(long entityId, long parentId);

    default void update(Entity entity, long entityId, long parentId) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    default Optional<Entity> getByName(String name, long parentId) {
        throw new UnsupportedOperationException("Unsupported operation");
    }
}