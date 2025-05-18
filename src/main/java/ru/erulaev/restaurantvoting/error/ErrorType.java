package ru.erulaev.restaurantvoting.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    BAD_REQUEST("Bad request", HttpStatus.UNPROCESSABLE_ENTITY);

    public final String title;
    public final HttpStatus status;

    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }
}
