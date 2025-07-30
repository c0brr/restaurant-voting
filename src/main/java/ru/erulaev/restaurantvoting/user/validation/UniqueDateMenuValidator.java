package ru.erulaev.restaurantvoting.user.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.repository.MenuRepository;

@Component
@AllArgsConstructor
public class UniqueDateMenuValidator implements org.springframework.validation.Validator {

    public static final String EXCEPTION_DUPLICATE_MENU = "Menu for this date already exists in a restaurant";

    private final MenuRepository menuRepository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Menu.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Menu menu = (Menu) target;
        long restaurantId = Long.parseLong(request.getRequestURI().split("/")[4]);
        menuRepository.getByRestaurantIdAndDate(restaurantId, menu.getDate())
                .ifPresent(dbMenu -> errors.rejectValue("date", "", EXCEPTION_DUPLICATE_MENU));
    }
}
