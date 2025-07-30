package ru.erulaev.restaurantvoting.user.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.erulaev.restaurantvoting.common.HasIdAndEmail;
import ru.erulaev.restaurantvoting.user.repository.UserRepository;

@Component
public class UniqueMailValidator extends AbstractValidator {

    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";

    private final UserRepository userRepository;

    protected UniqueMailValidator(HttpServletRequest request, UserRepository userRepository) {
        super(request);
        this.userRepository = userRepository;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail user = (HasIdAndEmail) target;
        if (StringUtils.hasText(user.getEmail())) {
            userRepository.getByEmailIgnoreCase(user.getEmail())
                    .ifPresent(dbUser ->
                            processEntity(user, dbUser, errors, EXCEPTION_DUPLICATE_EMAIL, "email"));
        }
    }
}