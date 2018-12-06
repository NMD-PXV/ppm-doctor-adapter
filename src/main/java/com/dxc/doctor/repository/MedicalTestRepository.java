package com.dxc.doctor.repository;

import com.dxc.doctor.entity.MedicalTestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalTestRepository extends JpaRepository<MedicalTestResultEntity, Long> {
    MedicalTestResultEntity findByIdEquals(Long medicalTestResultId);

    @Query("select m.allergicMedicines from MedicalTestResultEntity m where m.id = :id")
    String getProfileIdsByDisease(@Param("id") Long id);
}
