package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.user.model.User;

import java.util.List;
import java.util.Optional;

import static ru.erulaev.restaurantvoting.app.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContainingIgnoreCase(String name, Sort sort);

    @Cacheable("users")
    Optional<User> findByEmailIgnoreCase(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    int delete(long id);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    @SuppressWarnings("all") // transaction invoked
    default void deleteExisted(long id) {
        if (delete(id) == 0) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
    }

    @Transactional
    default User prepareAndSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return save(user);
    }
}