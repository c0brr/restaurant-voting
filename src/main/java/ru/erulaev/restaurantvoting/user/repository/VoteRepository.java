package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.user.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findAllByDate(LocalDate date);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.date = :date AND v.restaurant.id = :restaurantId")
    int findByDateAndRestaurantId(LocalDate date, long restaurantId);
}