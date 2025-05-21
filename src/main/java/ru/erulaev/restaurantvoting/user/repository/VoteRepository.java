package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.user.model.Vote;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

}