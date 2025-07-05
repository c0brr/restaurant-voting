package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Menu;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> getAll(long restaurantId);

    Optional<Menu> getById(long id, long restaurantId);

    @Transactional
    @Modifying
    int delete(long id, long restaurantId);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExisted(long id, long restaurantId) {
        if (delete(id, restaurantId) == 0) {
            throw new NotFoundException("Menu with id=" + id + " not found");
        }
    }
}