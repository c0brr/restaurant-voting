package ru.erulaev.restaurantvoting.user.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.erulaev.restaurantvoting.user.repository.UserRepository;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {

    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        userRepository.deleteExisted(id);
    }
}
