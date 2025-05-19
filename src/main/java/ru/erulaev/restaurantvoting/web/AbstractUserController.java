package ru.erulaev.restaurantvoting.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.erulaev.restaurantvoting.repository.UserRepository;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {

    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository userRepository;

    public void delete(int id) {
        log.info("delete {}", id);
        userRepository.deleteById(id);
    }
}
