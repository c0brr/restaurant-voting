package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.user.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> get(long id, long userId);

    List<Vote> findAllByCreated(LocalDate date);

    int getCountByCreatedAndRestaurantId(LocalDate created, long restaurantId);
}