package com.dxc.doctor.common;


import org.springframework.http.HttpStatus;

public enum MedicalProfilesStorageError {
    PATIENT_ID_NOT_FOUND(0, HttpStatus.NOT_FOUND),
    INVALID_INPUT(1, HttpStatus.BAD_REQUEST);

    private final int code;
    private final HttpStatus httpStatus;

    MedicalProfilesStorageError(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}