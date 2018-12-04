package com.dxc.doctor.repository;


import com.dxc.doctor.api.model.MedicalTestResult;
import com.dxc.doctor.entity.MedicalTestResultEntity;
import com.dxc.doctor.entity.MedicalTreatmentProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalProfileRepository extends JpaRepository<MedicalTreatmentProfileEntity, Long> {
    List<MedicalTreatmentProfileEntity> findByPatientIdEquals(String patientId);
}

