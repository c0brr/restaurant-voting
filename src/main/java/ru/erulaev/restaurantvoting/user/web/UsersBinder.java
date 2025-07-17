package ru.erulaev.restaurantvoting.user.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import ru.erulaev.restaurantvoting.user.web.admin.AdminUserController;
import ru.erulaev.restaurantvoting.user.web.regular.ProfileController;
import ru.erulaev.restaurantvoting.user.web.validation.UniqueMailValidator;

@ControllerAdvice(assignableTypes = {AdminUserController.class, ProfileController.class})
@AllArgsConstructor
public class UsersBinder {

    private final UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }
}
