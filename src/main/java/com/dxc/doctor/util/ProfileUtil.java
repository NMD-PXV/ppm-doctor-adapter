package com.dxc.doctor.util;

import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.entity.DiseasesHistory;
import com.dxc.doctor.entity.MedicalTreatmentProfileEntity;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.Collectors;

public class ProfileUtil {
    public static MedicalTreatmentProfile entity2Profile(MedicalTreatmentProfileEntity entity) {
        MedicalTreatmentProfile profile = new MedicalTreatmentProfile();
        profile.setId(new BigDecimal(entity.getId()));
        profile.setCreatedDate(LocalDate.fromDateFields(entity.getCreateDate()));
        profile.setModifiedDate(LocalDate.fromDateFields(entity.getModifiedDate()));
        profile.setDiseasesHistory(entity.getDiseasesHistory().stream().map(DiseasesHistory::getName).collect(Collectors.toList()));
        profile.setMedicalTestResult(TestResultUtil.entity2TestResult(entity.getMedicalTestResult()));
        profile.setDoctor(entity.getDoctor());
        profile.setDoctorUpdated(entity.getDoctorUpdated());
        profile.setPatientId(entity.getPatientId());
        profile.setPrescription(PrescriptionUtil.entity2Prescription(entity.getPrescription()));
        return profile;
    }

    public static MedicalTreatmentProfileEntity profile2entity(MedicalTreatmentProfile profile) {
       return MedicalTreatmentProfileEntity.builder()
                .doctor(profile.getDoctor())
                .doctorUpdated(profile.getDoctor())
                .createDate(new Date())
                .modifiedDate(new Date())
                .patientId(profile.getPatientId())
                .diseasesHistory(profile.getDiseasesHistory().stream().map(name -> DiseasesHistory.builder().name(name).build()).collect(Collectors.toList()))
                .medicalTestResult(TestResultUtil.testResult2Entity(profile.getMedicalTestResult()))
                .prescription(PrescriptionUtil.prescription2Entity(profile.getPrescription())).build();
    }

    public static MedicalTreatmentProfileEntity updateProfile (MedicalTreatmentProfileEntity profileEntity,
                                                               MedicalTreatmentProfile profile) {
            profileEntity.setDoctorUpdated(profile.getDoctorUpdated());
            profileEntity.setModifiedDate(new Date());
            profileEntity.setDiseasesHistory(profile.getDiseasesHistory().stream().map(name ->
                    DiseasesHistory.builder().name(name).build()).collect(Collectors.toList()));
        return profileEntity;
    }
}
