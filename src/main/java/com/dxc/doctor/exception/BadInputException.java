package com.dxc.doctor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class BadInputException extends RuntimeException{
    public BadInputException(String exception) {
        super(exception);
    }
}
