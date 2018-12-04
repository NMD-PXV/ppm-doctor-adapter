package com.dxc.doctor.util;


import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.api.model.MedicalTestResult;
import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.entity.GivenMedicineEntity;
import com.dxc.doctor.entity.MedicalTestResultEntity;

import java.util.ArrayList;
import java.util.List;

public class Converter {



    public static List<GivenMedicineEntity> convertGivenMedicineToEntity(List<GivenMedicine> givenMedicines,
    String type) {
        List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
        givenMedicines.stream().forEach(givenMedicineMapper -> {
            GivenMedicineEntity givenMedicineEntity = new GivenMedicineEntity();
            givenMedicineEntity.setName(givenMedicineMapper.getName());
            givenMedicineEntity.setQuantity(givenMedicineMapper.getQuantity());
            givenMedicineEntity.setType(type);
            givenMedicineEntityList.add(givenMedicineEntity);
        });
        return givenMedicineEntityList;
    }

    public static MedicalTestResultEntity convertMedicalTestResultToEntity(MedicalTreatmentProfile profile) {
        MedicalTestResultEntity medicalTestResultEntity = new MedicalTestResultEntity();
        medicalTestResultEntity.setBloodType(profile.getMedicalTestResult().getBloodType());
        medicalTestResultEntity.setxRay(profile.getMedicalTestResult().getXRay());
        medicalTestResultEntity.setUltraSound(profile.getMedicalTestResult().getUltraSound());
        medicalTestResultEntity.setAllergicMedicines(profile.getMedicalTestResult().getAllergicMedicines().toString());
        return medicalTestResultEntity;
    }

    public static MedicalTestResultEntity updateMedicalTestResult(MedicalTestResultEntity medicalTestResultEntity, MedicalTreatmentProfile profile) {
        medicalTestResultEntity.setBloodType(profile.getMedicalTestResult().getBloodType());
        medicalTestResultEntity.setxRay(profile.getMedicalTestResult().getXRay());
        medicalTestResultEntity.setUltraSound(profile.getMedicalTestResult().getUltraSound());
        medicalTestResultEntity.setAllergicMedicines(profile.getMedicalTestResult().getAllergicMedicines().toString());
        return medicalTestResultEntity;
    }


}
