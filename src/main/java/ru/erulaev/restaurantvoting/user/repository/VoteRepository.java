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
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> getByUserIdAndDate(long userId, LocalDate date);

    List<Vote> findAllByUserId(long userId);

    List<Vote> findAllByDate(LocalDate date);

    int getCountByDateAndRestaurantId(LocalDate date, long restaurantId);

    @Transactional
    @Modifying
    int delete(long userId, LocalDate date);

    default void deleteExisted(long userId, LocalDate date) {
        if (delete(userId, date) == 0) {
            throw new NotFoundException("Vote from user with id= " + userId + " not found for today");
        }
    }
}