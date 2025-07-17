package ru.erulaev.restaurantvoting.user.web.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;
import ru.erulaev.restaurantvoting.app.AuthUtil;
import ru.erulaev.restaurantvoting.common.HasId;
import ru.erulaev.restaurantvoting.common.HasIdAndEmail;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractValidator implements org.springframework.validation.Validator {

    protected final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasId.class.isAssignableFrom(clazz);
    }

    protected void processEntity(HasId requestEntity, HasId dbEntity, Errors errors, String msg, String filed) {
        if (request.getMethod().equals("PUT")) { // UPDATE
            long dbId = dbEntity.id();

            // it is ok, if update by ourselves
            if (requestEntity.getId() != null && dbId == requestEntity.id()) {
                return;
            }

            String requestURI = request.getRequestURI();
            if (requestURI.endsWith("/" + dbId)) {
                return;
            }
            if (requestEntity instanceof HasIdAndEmail && dbId == AuthUtil.get().id() && requestURI.contains("/profile")) {
                return;
            }
        }
        errors.rejectValue(filed, "", msg);
    }
}
