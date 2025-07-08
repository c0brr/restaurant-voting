package ru.erulaev.restaurantvoting.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;

import java.util.Optional;

@NoRepositoryBean
public interface FoodBaseRepository<T> extends JpaRepository<T, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id = ?1 AND e.parentEntity.id = ?2")
    int delete(long id, long parentId);

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = ?1 AND e.parentEntity.id = ?2")
    Optional<T> get(long id, long parentId);

    @SuppressWarnings("all")
    default void deleteExisted(long id, long parentId) {
        if (delete(id, parentId) == 0) {
            throw new NotFoundException("Entity with id=" + id +
                    " not found in parent entity with id=" + parentId);
        }
    }

    default T getExisted(long id, long parentId) {
        return get(id, parentId).orElseThrow(() -> new NotFoundException("Entity with id=" + id +
                " not found in parent entity with id=" + parentId));
    }
}