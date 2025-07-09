package ru.erulaev.restaurantvoting.user.web.admin;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.erulaev.restaurantvoting.user.model.User;
import ru.erulaev.restaurantvoting.user.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController extends AbstractCoreEntityController<User, UserRepository> {

    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "name", "email");
    static final String REST_URL = "/api/admin/users";

    public AdminUserController(UserRepository repository) {
        super(repository);
    }

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        return super.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        return super.createWithLocation(user, REST_URL);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", allEntries = true)
    public void delete(@PathVariable long id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", key = "#user.email")
    public void update(@Valid @RequestBody User user, @PathVariable long id) {
        super.update(user, id);
    }

    @GetMapping("/by-email")
    public ResponseEntity<User> getByEmail(@RequestParam String email) {
        log.info("getByEmail {}", email);
        return ResponseEntity.of(repository.findByEmailIgnoreCase(email));
    }

    @GetMapping("/by-containing-name")
    public List<User> getByContainingName(@RequestParam String name) {
        return super.getByContainingName(name, SORT);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable long id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = repository.getExisted(id);
        user.setEnabled(enabled);
    }
}