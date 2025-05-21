package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.user.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

}