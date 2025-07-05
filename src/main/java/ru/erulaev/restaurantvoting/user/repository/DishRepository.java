package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> getAll(long menuId);

    Optional<Dish> getById(long id, long menuId);

    @Transactional
    @Modifying
    int delete(long id, long menuId);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExisted(long id, long menuId) {
        if (delete(id, menuId) == 0) {
            throw new NotFoundException("Dish with id=" + id + " not found");
        }
    }

    @Transactional
    default Dish prepareAndSave(Dish dish) {
        dish.setName(dish.getName().toLowerCase());
        return save(dish);
    }

    @Query("SELECT d FROM Dish d WHERE d.name = LOWER(:name) AND d.menu.id = :menuId")
    Optional<Dish> findByNameIgnoreCase(String name, long menuId);
}