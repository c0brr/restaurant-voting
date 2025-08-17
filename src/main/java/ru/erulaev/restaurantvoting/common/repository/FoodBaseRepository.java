package ru.erulaev.restaurantvoting.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;

import java.util.Optional;

@NoRepositoryBean
public interface FoodBaseRepository<T> extends JpaRepository<T, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id = ?1 AND e.parentEntity.id = ?2")
    int delete(int id, int parentId);

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = ?1 AND e.parentEntity.id = ?2")
    Optional<T> get(int id, int parentId);

    @SuppressWarnings("SpringTransactionalMethodCallsInspection")
    default void deleteExisted(int id, int parentId) {
        if (delete(id, parentId) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found at parent entity with id=" + parentId);
        }
    }

    default T getExisted(int id, int parentId) {
        return get(id, parentId).orElseThrow(
                () -> new NotFoundException("Entity with id=" + id + " not found at parent entity with id=" + parentId));
    }
}