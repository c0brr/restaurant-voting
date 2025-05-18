package ru.erulaev.restaurantvoting.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.erulaev.restaurantvoting.error.AppException;

import java.net.URI;

@RestControllerAdvice
@AllArgsConstructor
@Getter
@Slf4j
public class RestExceptionHandler {

    public static final String ERR_PFX = "ERR# ";
    private final MessageSource messageSource;

    @ExceptionHandler(AppException.class)
    ProblemDetail appException(AppException ex, HttpServletRequest request) {
        log.error(ERR_PFX + "Exception {} at request {}", ex, request.getRequestURI(), ex);
        ErrorResponse.Builder builder = ErrorResponse.builder(ex, ex.getErrorType().status,
                "Exception " + ex.getClass().getSimpleName());
        return builder
                .title(ex.getErrorType().title).instance(URI.create(request.getRequestURI()))
                .build().updateAndGetBody(messageSource, LocaleContextHolder.getLocale());
    }
}
