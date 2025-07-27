package ru.erulaev.restaurantvoting.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    APP_ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_DATA("Wrong data", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_REQUEST("Invalid request", HttpStatus.UNPROCESSABLE_ENTITY),
    WRONG_REQUEST("Bad request", HttpStatus.BAD_REQUEST),
    DATA_CONFLICT("DataBase conflict", HttpStatus.CONFLICT),
    NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("Request unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("Request forbidden", HttpStatus.FORBIDDEN);

    public final String title;
    public final HttpStatus status;

    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }
}