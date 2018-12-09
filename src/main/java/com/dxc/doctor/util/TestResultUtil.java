package com.dxc.doctor.util;

import com.dxc.doctor.api.model.MedicalTestResult;
import com.dxc.doctor.entity.MedicalTestResultEntity;
import com.dxc.doctor.entity.MedicalTreatmentProfileEntity;

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
        testResult.setXRay(entity.getXRay());
        return testResult;
    }

    public static MedicalTestResultEntity testResult2Entity(MedicalTestResult testResult) {
       return MedicalTestResultEntity.builder()
               .bloodType(testResult.getBloodType())
               .allergicMedicines(testResult.getAllergicMedicines().stream().collect(Collectors.joining(",")))
               .ultraSound(testResult.getUltraSound())
               .xRay(testResult.getXRay()).build();
    }

    public static MedicalTestResultEntity updateMedicalResult(MedicalTestResult testResult) {
        MedicalTestResultEntity medicalReult = testResult2Entity(testResult);
        medicalReult.setId(testResult.getId().longValue());
        return medicalReult;
    }
}
