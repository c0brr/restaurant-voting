package ru.erulaev.restaurantvoting.user.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.erulaev.restaurantvoting.common.model.NamedEntity;
import ru.erulaev.restaurantvoting.user.repository.DishRepository;
import ru.erulaev.restaurantvoting.user.util.NameUtil;

@Component
public class UniqueDishNameValidator extends AbstractValidator {

    public static final String EXCEPTION_DUPLICATE_NAME = "Dish with this name already exists in menu";

    private final DishRepository dishRepository;

    protected UniqueDishNameValidator(HttpServletRequest request, DishRepository dishRepository) {
        super(request);
        this.dishRepository = dishRepository;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        NamedEntity dish = (NamedEntity) target;
        int menuId = Integer.parseInt(request.getRequestURI().split("/")[4]);
        if (StringUtils.hasText(dish.getName())) {
            dishRepository.getByMenuIdAndName(menuId, NameUtil.getCorrectName(dish.getName()))
                    .ifPresent(dbDish -> processEntity(dish, dbDish, errors, EXCEPTION_DUPLICATE_NAME, "name"));
        }
    }
}
