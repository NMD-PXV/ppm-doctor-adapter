package com.dxc.doctor.repository;

import com.dxc.doctor.entity.MedicalTreatmentProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MedicalProfileRepository extends JpaRepository<MedicalTreatmentProfileEntity, Long> {
    List<MedicalTreatmentProfileEntity> findByPatientIdEquals(String patientId);

    @Query("select p from MedicalTreatmentProfileEntity p where p.id in :ids")
    List<MedicalTreatmentProfileEntity> findMultiProfiles(@Param("ids") Set<Long> ids);

    MedicalTreatmentProfileEntity findByIdEquals(Long id);

    @Query("select p.id from MedicalTreatmentProfileEntity p where p.patientId in :ids")
    Set<Long> findProfileIdsByPatientId(@Param("ids") List<String> ids);

    @Query("select p from MedicalTreatmentProfileEntity p where p.id in :ids")
    List<MedicalTreatmentProfileEntity> findMultiProfilesById(@Param("ids") Set<Long> ids);

}

