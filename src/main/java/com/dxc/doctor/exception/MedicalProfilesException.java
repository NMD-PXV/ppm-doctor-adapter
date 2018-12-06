package com.dxc.doctor.exception;

import com.dxc.doctor.common.MedicalProfilesStorageError;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MedicalProfilesException extends RuntimeException{
    private final MedicalProfilesStorageError response;

    public MedicalProfilesException(MedicalProfilesStorageError response) {
        this(response, null, new Object[0]);
    }

    public MedicalProfilesException(MedicalProfilesStorageError response, Throwable cause, Object... params) {
        super(response + Arrays.stream(params).map(Object::toString).collect(Collectors.joining(";", "{", "}")));
        this.response = response;
    }

    public MedicalProfilesException(MedicalProfilesStorageError response, Object... params) {
        this(response, null, params);
    }

    public MedicalProfilesStorageError getResponse() {
        return response;
    }
}
