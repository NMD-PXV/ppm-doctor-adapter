package com.dxc.doctor.handler;

import com.dxc.doctor.exception.BadInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;

@ControllerAdvice
public class BadInputExceptionHandler  {
    @ExceptionHandler(BadInputException.class)
    public ResponseEntity<String> handleBadInputException(BadInputException exception, WebRequest res) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                exception.getMessage(), res.getDescription(false));
        return new ResponseEntity<>(errorDetails.toString(), HttpStatus.BAD_REQUEST);
    }
}
