package ru.erulaev.restaurantvoting.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.erulaev.restaurantvoting.user.repository.UserRepository;

public class AbstractUserControllerTest extends AbstractControllerTest {

    @Autowired
    protected UserRepository userRepository;
}
