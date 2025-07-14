package ru.erulaev.restaurantvoting.user.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import ru.erulaev.restaurantvoting.common.error.NotFoundException;
import ru.erulaev.restaurantvoting.common.repository.CoreEntityBaseRepository;
import ru.erulaev.restaurantvoting.user.model.User;

import java.util.List;
import java.util.Optional;

import static ru.erulaev.restaurantvoting.app.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface UserRepository extends CoreEntityBaseRepository<User> {

    @Override
    List<User> getAll();

    @Cacheable("users")
    Optional<User> findByEmailIgnoreCase(String email);

    @Override
    @Transactional
    default User prepareAndSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return save(user);
    }

    @SuppressWarnings("all")
    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(
                () -> new NotFoundException("User with email=" + email + " not found"));
    }

    @Override
    default User getExisted(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
    }
}