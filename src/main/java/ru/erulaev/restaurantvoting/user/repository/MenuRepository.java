package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.user.model.Menu;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

}
