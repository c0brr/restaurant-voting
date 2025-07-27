package ru.erulaev.restaurantvoting.common.error;

import static ru.erulaev.restaurantvoting.common.error.ErrorType.INVALID_REQUEST;

public class IllegalRequestDataException extends AppException {

    public IllegalRequestDataException(String msg) {
        super(msg, INVALID_REQUEST);
    }
}