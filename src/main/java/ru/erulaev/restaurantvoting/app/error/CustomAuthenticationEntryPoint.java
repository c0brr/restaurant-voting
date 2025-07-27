package ru.erulaev.restaurantvoting.app.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static ru.erulaev.restaurantvoting.common.error.ErrorType.UNAUTHORIZED;

@Component
public class CustomAuthenticationEntryPoint extends AbstractSecurityExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        log.warn("Auth failed for {} {}, IP: {},", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        response.setHeader("WWW-Authenticate", "Basic realm=\"Restaurant voting API\"");
        createProblemDetail(request, response, ex, UNAUTHORIZED, "Authorization required for request");
    }
}