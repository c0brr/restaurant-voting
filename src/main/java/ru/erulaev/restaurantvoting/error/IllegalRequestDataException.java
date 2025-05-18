package ru.erulaev.restaurantvoting.error;

import static ru.erulaev.restaurantvoting.error.ErrorType.BAD_REQUEST;

public class IllegalRequestDataException extends AppException {

    public IllegalRequestDataException(String msg) {
        super(msg, BAD_REQUEST);
    }
}