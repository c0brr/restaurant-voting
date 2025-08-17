package ru.erulaev.restaurantvoting.common.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;

import java.util.List;

// https://stackoverflow.com/questions/42781264/multiple-base-repositories-in-spring-data-jpa
@NoRepositoryBean
public interface CoreEntityBaseRepository<T> extends JpaRepository<T, Integer> {

    List<T> getAll();

    T prepareAndSave(T entity);

    T getExisted(int id);

    List<T> findByNameContainingIgnoreCase(String name, Sort sort);

    //    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query.spel-expressions
    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id = :id")
    int delete(int id);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("SpringTransactionalMethodCallsInspection") // transaction invoked
    default void deleteExisted(int id) {
        if (delete(id) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }
}
