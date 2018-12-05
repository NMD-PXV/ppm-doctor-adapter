package com.dxc.doctor.util;

import com.dxc.doctor.api.model.MedicalTestResult;
import com.dxc.doctor.entity.MedicalTestResultEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TestResultUtil {
    public static MedicalTestResult entity2TestResult(MedicalTestResultEntity entity){
        MedicalTestResult testResult = new MedicalTestResult();
        testResult.setAllergicMedicines(Arrays.stream(entity.getAllergicMedicines().split(",")).collect(Collectors.toList()));
        testResult.setBloodType(entity.getBloodType());
        testResult.setUltraSound(entity.getUltraSound());
        testResult.setXRay(entity.getxRay());
        return testResult;
    }
}
