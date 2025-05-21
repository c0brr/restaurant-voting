package ru.erulaev.restaurantvoting.common.error;

import static ru.erulaev.restaurantvoting.common.error.ErrorType.BAD_REQUEST;

public class IllegalRequestDataException extends AppException {

    public IllegalRequestDataException(String msg) {
        super(msg, BAD_REQUEST);
    }
}