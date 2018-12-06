package com.dxc.doctor.repository;

import com.dxc.doctor.entity.MedicalTestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MedicalTestRepository extends JpaRepository<MedicalTestResultEntity, Long> {
    MedicalTestResultEntity findByIdEquals(Long medicalTestResultId);

    @Query("select m.allergic_medicines from MedicalTestResultEntity m where d.name = :name")
    Set<Long> getProfileIdsByDisease(@Param("name") String name);
}
