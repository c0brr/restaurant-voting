package ru.erulaev.restaurantvoting.app.error;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static ru.erulaev.restaurantvoting.common.error.ErrorType.FORBIDDEN;

@Component
public class CustomAccessDeniedHandler extends AbstractSecurityExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {
        log.warn("Access denied for {} {} from user '{}' (roles: {})", request.getMethod(), request.getRequestURI(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        createProblemDetail(request, response, ex, FORBIDDEN, "Insufficient permissions to perform request");
    }
}
