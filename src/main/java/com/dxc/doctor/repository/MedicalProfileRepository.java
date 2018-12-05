package com.dxc.doctor.repository;

import com.dxc.doctor.entity.MedicalTreatmentProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public interface MedicalProfileRepository extends JpaRepository<MedicalTreatmentProfileEntity, Long> {
    List<MedicalTreatmentProfileEntity> findByPatientIdEquals(String patientId);

    @Query("select p from MedicalTreatmentProfile where p.id in :ids")
    List<MedicalTreatmentProfileEntity> findMultiProfiles(@Param("ids") HashSet<Long> ids);
}

