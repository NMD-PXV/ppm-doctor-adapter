package com.dxc.doctor.repository;

import com.dxc.doctor.entity.MedicalTestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalTestRepository extends JpaRepository<MedicalTestResultEntity, Long> {
    MedicalTestResultEntity findByIdEquals(Long medicalTestResultId);
}
