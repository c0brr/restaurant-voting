package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.user.model.Dish;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Long> {

}
