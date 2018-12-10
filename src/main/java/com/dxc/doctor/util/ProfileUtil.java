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

        MedicalTreatmentProfileEntity profileEntity = new MedicalTreatmentProfileEntity();
        profileEntity.setDoctor(profile.getDoctor());
        profileEntity.setDoctorUpdated(profile.getDoctor());
        profileEntity.setCreateDate(new Date());
        profileEntity.setModifiedDate(new Date());
        profileEntity.setPatientId(profile.getPatientId());
        profileEntity.setDiseasesHistory(profile.getDiseasesHistory().stream().map(DiseasesHistory::new).collect(Collectors.toList()));
        profileEntity.setMedicalTestResult(TestResultUtil.testResult2Entity(profile.getMedicalTestResult()));
        profileEntity.setPrescription(PrescriptionUtil.prescription2Entity(profile.getPrescription()));
        return profileEntity;
    }

    public static MedicalTreatmentProfileEntity updateProfile (MedicalTreatmentProfileEntity profileEntity,
                                                               MedicalTreatmentProfile profile) {
        profileEntity.setDoctorUpdated(profile.getDoctorUpdated());
        profileEntity.setModifiedDate(new Date());
        profileEntity.setDiseasesHistory(profile.getDiseasesHistory().stream().map(DiseasesHistory::new)
                .collect(Collectors.toList()));
        return profileEntity;
    }
}
