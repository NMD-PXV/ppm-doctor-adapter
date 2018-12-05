package com.dxc.doctor.util;

import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.entity.MedicalTreatmentProfileEntity;

import java.util.Objects;
import java.util.stream.Collectors;

public class ProfileUtil {
    public static MedicalTreatmentProfile entity2Profile(MedicalTreatmentProfileEntity entity) {
        MedicalTreatmentProfile profile = new MedicalTreatmentProfile();
        profile.setDiseasesHistory(entity.getDiseasesHistory().stream().map(Objects::toString).collect(Collectors.toList()));
        profile.setMedicalTestResult(TestResultUtil.entity2TestResult(entity.getMedicalTestResult()));
        profile.setDoctor(entity.getDoctor());
        profile.setDoctorUpdated(entity.getDoctorUpdated());
        profile.setPrescription(PrescriptionUtil.entity2Prescription(entity.getPrescription()));
        return profile;
    }
}
