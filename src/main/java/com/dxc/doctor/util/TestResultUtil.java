package com.dxc.doctor.util;

import com.dxc.doctor.api.model.MedicalTestResult;
import com.dxc.doctor.entity.MedicalTestResultEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestResultUtil {
    public static MedicalTestResult entity2TestResult(MedicalTestResultEntity entity){
        MedicalTestResult testResult = new MedicalTestResult();
        testResult.setId(new BigDecimal(entity.getId()));
        testResult.setAllergicMedicines(Arrays.stream(entity.getAllergicMedicines().split(",")).collect(Collectors.toList()));
        testResult.setBloodType(entity.getBloodType());
        testResult.setUltraSound(entity.getUltraSound());
        testResult.setXRay(entity.getxRay());
        return testResult;
    }

    public static MedicalTestResultEntity testResult2Entity(MedicalTestResult testResult) {
        MedicalTestResultEntity entity = new MedicalTestResultEntity();
        entity.setBloodType(testResult.getBloodType());
        entity.setxRay(testResult.getXRay());
        entity.setUltraSound(testResult.getUltraSound());
        entity.setAllergicMedicines(testResult.getAllergicMedicines().stream()
                .collect(Collectors.joining(",")));
        return entity;
    }
}
