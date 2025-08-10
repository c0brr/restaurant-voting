package ru.erulaev.restaurantvoting.app.error;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.exc.InputCoercionException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.erulaev.restaurantvoting.common.error.AppException;
import ru.erulaev.restaurantvoting.common.error.ErrorType;

import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static ru.erulaev.restaurantvoting.common.error.ErrorType.*;

@RestControllerAdvice
@AllArgsConstructor
@Getter
@Slf4j
public class RestExceptionHandler {

    public static final String ERR_PFX = "ERR# ";

    private final MessageSource messageSource;

    //    https://stackoverflow.com/a/52254601/548473
    static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP = new LinkedHashMap<>() {
        {
// more specific first
            put(NoResourceFoundException.class, NOT_FOUND);
            put(AuthenticationException.class, UNAUTHORIZED);
            put(FileNotFoundException.class, NOT_FOUND);
            put(NoHandlerFoundException.class, NOT_FOUND);
            put(UnsupportedOperationException.class, APP_ERROR);
            put(EntityNotFoundException.class, DATA_CONFLICT);
            put(DataIntegrityViolationException.class, DATA_CONFLICT);
            put(IllegalArgumentException.class, BAD_DATA);
            put(ValidationException.class, INVALID_REQUEST);
            put(HttpRequestMethodNotSupportedException.class, INVALID_REQUEST);
            put(ServletRequestBindingException.class, INVALID_REQUEST);
            put(RequestRejectedException.class, INVALID_REQUEST);
            put(AccessDeniedException.class, FORBIDDEN);
        }
    };

    @ExceptionHandler(BindException.class)
    ProblemDetail bindException(BindException ex, HttpServletRequest request) {
        Map<String, String> invalidParams = getErrorMap(ex.getBindingResult());
        String path = request.getRequestURI();
        log.warn(ERR_PFX + "BindException with invalidParams {} at request {}", invalidParams, path);
        return createProblemDetail(ex, path, INVALID_REQUEST, "BindException", Map.of("invalid_params", invalidParams));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail httpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Throwable cause = getRootCause(ex);
        return cause.getClass().isAssignableFrom(JsonParseException.class) ? requestBodyException(ex, request) : exception(ex, request);
    }

    @ExceptionHandler({JsonEOFException.class, InputCoercionException.class})
    ProblemDetail requestBodyException(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.warn(ERR_PFX + "{} at request {}", ex.getClass().getSimpleName(), path);
        return createProblemDetail(ex, path, BAD_REQUEST, "Malformed JSON request", Map.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        Throwable cause = getRootCause(ex);
        if (cause.getClass().isAssignableFrom(DateTimeParseException.class)) {
            Optional<String> format = Optional.of(ex.getParameter())
                    .map(parameter -> parameter.getParameterAnnotation(DateTimeFormat.class))
                    .map(annotation -> switch (annotation.iso()) {
                        case DATE -> "yyyy-MM-dd";
                        case TIME -> "HH:mm:ss.SSSZ";
                        case DATE_TIME -> "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
                        case NONE -> null;
                    });
            if (format.isPresent()) {
                String parameter = ex.getName();
                String errorMessage = "Invalid format for parameter '" + parameter + "'" + ". Expected format: '" + format.get() + "'";
                String path = request.getRequestURI();
                log.warn(ERR_PFX + "DateTimeParseException at parameter '{}' at request {}", parameter, path);
                return createProblemDetail(cause, path, BAD_REQUEST, errorMessage, Map.of());
            }
        }
        return exception(ex, request);
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> invalidParams = new LinkedHashMap<>();
        for (ObjectError error : result.getGlobalErrors()) {
            invalidParams.put(error.getObjectName(), getErrorMessage(error));
        }
        for (FieldError error : result.getFieldErrors()) {
            invalidParams.put(error.getField(), getErrorMessage(error));
        }
        return invalidParams;
    }

    private String getErrorMessage(ObjectError error) {
        return error.getCode() == null ? error.getDefaultMessage() :
                messageSource.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail exception(Exception ex, HttpServletRequest request) {
        return processException(ex, request, Map.of());
    }

    ProblemDetail processException(@NonNull Throwable ex, HttpServletRequest request, Map<String, Object> additionalParams) {
        Optional<ErrorType> optType = findErrorType(ex);
        if (optType.isEmpty()) {
            Throwable root = getRootCause(ex);
            if (root != ex) {
                optType = findErrorType(root);
                ex = root;
            }
        }
        String path = request.getRequestURI();
        if (optType.isPresent()) {
            log.error(ERR_PFX + "Exception {} at request {}", ex, path);
            return createProblemDetail(ex, path, optType.get(), ex.getMessage(), additionalParams);
        } else {
            Throwable root = getRootCause(ex);
            log.error(ERR_PFX + "Exception {} at request {}", root, path, root);
            return createProblemDetail(ex, path, APP_ERROR, "Exception " + root.getClass().getSimpleName(), additionalParams);
        }
    }

    private Optional<ErrorType> findErrorType(Throwable ex) {
        if (ex instanceof AppException aex) {
            return Optional.of(aex.getErrorType());
        }
        Class<? extends Throwable> exClass = ex.getClass();
        return HTTP_STATUS_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(exClass))
                .findAny().map(Map.Entry::getValue);
    }

    //    https://datatracker.ietf.org/doc/html/rfc7807
    private ProblemDetail createProblemDetail(Throwable ex, String path, ErrorType type, String defaultDetail, @NonNull Map<String, Object> additionalParams) {
        ErrorResponse.Builder builder = ErrorResponse.builder(ex, type.status, defaultDetail);
        ProblemDetail pd = builder
                .title(type.title).instance(URI.create(path))
                .build().updateAndGetBody(messageSource, LocaleContextHolder.getLocale());
        additionalParams.forEach(pd::setProperty);
        return pd;
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    private static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}