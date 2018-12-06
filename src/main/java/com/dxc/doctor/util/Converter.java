package com.dxc.doctor.util;


import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.api.model.MedicalTestResult;
import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.GivenMedicineEntity;
import com.dxc.doctor.entity.MedicalTestResultEntity;
import com.dxc.doctor.entity.MedicalTreatmentProfileEntity;
import com.dxc.doctor.entity.PrescriptionEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Converter {



    public static List<GivenMedicineEntity> convertMedicinesToEntity(List<GivenMedicine> givenMedicines,
    String type) {
        List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
        givenMedicines.stream().forEach(givenMedicineMapper -> {
            GivenMedicineEntity givenMedicineEntity = new GivenMedicineEntity();
            givenMedicineEntity.setName(givenMedicineMapper.getName());
            givenMedicineEntity.setQuantity(givenMedicineMapper.getQuantity());
            givenMedicineEntity.setType(type);
            givenMedicineEntity.setDeleted(false);
            givenMedicineEntityList.add(givenMedicineEntity);
        });
        return givenMedicineEntityList;
    }

    public static List<GivenMedicineEntity> convertMedicinesForUpdate(List<GivenMedicine> givenMedicines,
                                                                         String type) {
        List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
        givenMedicines.stream().forEach(givenMedicineMapper -> {
            GivenMedicineEntity givenMedicineEntity = new GivenMedicineEntity();
            givenMedicineEntity.setId(givenMedicineMapper.getId().longValue());
            givenMedicineEntity.setName(givenMedicineMapper.getName());
            givenMedicineEntity.setQuantity(givenMedicineMapper.getQuantity());
            givenMedicineEntity.setType(type);
            givenMedicineEntity.setDeleted(false);
            givenMedicineEntityList.add(givenMedicineEntity);
        });
        return givenMedicineEntityList;
    }

    public static MedicalTestResultEntity convertMedicalTestResultToEntity(MedicalTreatmentProfile profile) {
        MedicalTestResultEntity medicalTestResultEntity = new MedicalTestResultEntity();
        medicalTestResultEntity.setBloodType(profile.getMedicalTestResult().getBloodType());
        medicalTestResultEntity.setxRay(profile.getMedicalTestResult().getXRay());
        medicalTestResultEntity.setUltraSound(profile.getMedicalTestResult().getUltraSound());
        medicalTestResultEntity.setAllergicMedicines(profile.getMedicalTestResult().getAllergicMedicines().stream().collect(Collectors.joining(",")));
        return medicalTestResultEntity;
    }

    public static MedicalTestResultEntity convertMedicalTestResultForUpdate(MedicalTestResultEntity medicalTestResultEntity, MedicalTreatmentProfile profile) {
        medicalTestResultEntity.setBloodType(profile.getMedicalTestResult().getBloodType());
        medicalTestResultEntity.setxRay(profile.getMedicalTestResult().getXRay());
        medicalTestResultEntity.setUltraSound(profile.getMedicalTestResult().getUltraSound());
        medicalTestResultEntity.setAllergicMedicines(profile.getMedicalTestResult().getAllergicMedicines().stream().collect(Collectors.joining(",")));
        return medicalTestResultEntity;
    }

    public static BigDecimal convertBigDecimalToLong(Long id) {
        BigDecimal bId = new BigDecimal(id.longValue());
        return bId;
    }


}
