package com.dxc.doctor.repository;

import com.dxc.doctor.entity.PrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, Long> {
    PrescriptionEntity findByIdEquals(Long prescriptionId);
}
