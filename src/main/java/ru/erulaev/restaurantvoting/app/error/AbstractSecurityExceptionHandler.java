package ru.erulaev.restaurantvoting.app.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import ru.erulaev.restaurantvoting.common.error.ErrorType;

import java.io.IOException;
import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractSecurityExceptionHandler {

    protected final Logger log = getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    protected void createProblemDetail(HttpServletRequest request, HttpServletResponse response,
                                       Throwable ex, ErrorType type, String message) throws IOException {
        String path = request.getRequestURI();
        response.setContentType("application/problem+json");
        response.setStatus(type.status.value());

        ProblemDetail pd = ErrorResponse.builder(ex, type.status, message)
                .title(type.title)
                .instance(URI.create(path))
                .build().updateAndGetBody(messageSource, LocaleContextHolder.getLocale());

        objectMapper.writeValue(response.getWriter(), pd);
    }
}
