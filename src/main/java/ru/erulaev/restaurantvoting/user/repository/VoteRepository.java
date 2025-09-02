package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.entity.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> getByUserIdAndCreationDate(int userId, LocalDate creationDate);

    List<Vote> getAllByUserId(int userId);

    int getCountByCreationDateAndRestaurantId(LocalDate creationDate, int restaurantId);

    List<Vote> findAllByCreationDate(LocalDate creationDate);

    boolean existsByUserIdAndCreationDate(int userId, LocalDate creationDate);

    @Transactional
    @Modifying
    int delete(int userId, LocalDate creationDate);

    default void deleteExisted(int userId, LocalDate creationDate) {
        if (delete(userId, creationDate) == 0) {
            throw new NotFoundException("Vote from user with id= " + userId + " not found for today");
        }
    }
}