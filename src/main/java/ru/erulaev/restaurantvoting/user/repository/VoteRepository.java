package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> getByUserIdAndDate(int userId, LocalDate date);

    List<Vote> getAllByUserId(int userId);

    int getCountByDateAndRestaurantId(LocalDate date, int restaurantId);

    List<Vote> findAllByDate(LocalDate date);

    boolean existsByUserIdAndDate(int userId, LocalDate date);

    @Transactional
    @Modifying
    int delete(int userId, LocalDate date);

    default void deleteExisted(int userId, LocalDate date) {
        if (delete(userId, date) == 0) {
            throw new NotFoundException("Vote from user with id= " + userId + " not found for today");
        }
    }
}