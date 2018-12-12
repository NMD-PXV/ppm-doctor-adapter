package com.dxc.ppm.treatmentadapter.handler;

import com.dxc.ppm.treatmentadapter.exception.MedicalProfilesException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class MedicalProfilesExceptionHandler {

    @ExceptionHandler(MedicalProfilesException.class)
    public ResponseEntity<String> personalInfoExceptionHandle(MedicalProfilesException ex) {
        String message = ex.getResponse().name();
        return new ResponseEntity<>(message, ex.getResponse().getHttpStatus());
    }
}
