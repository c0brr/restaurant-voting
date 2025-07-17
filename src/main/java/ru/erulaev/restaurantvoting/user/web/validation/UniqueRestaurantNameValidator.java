package ru.erulaev.restaurantvoting.user.web.validation;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;
import ru.erulaev.restaurantvoting.user.repository.RestaurantRepository;

@Component
public class UniqueRestaurantNameValidator extends AbstractValidator {

    public static final String EXCEPTION_DUPLICATE_NAME = "Restaurant with this name already exists";

    private final RestaurantRepository restaurantRepository;

    protected UniqueRestaurantNameValidator(HttpServletRequest request, RestaurantRepository restaurantRepository) {
        super(request);
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        NamedEntity restaurant = (NamedEntity) target;
        if (StringUtils.hasText(restaurant.getName())) {
            restaurantRepository.findByNameIgnoreCase(restaurant.getName())
                    .ifPresent(dbRestaurant ->
                            processEntity(restaurant, dbRestaurant, errors, EXCEPTION_DUPLICATE_NAME, "name"));
        }
    }
}
