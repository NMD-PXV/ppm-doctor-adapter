package com.dxc.ppm.treatmentadapter.repository;

import com.dxc.ppm.treatmentadapter.entity.PrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, Long> {
    PrescriptionEntity findByIdEquals(Long prescriptionId);

    @Query("select p.medicalTreatmentProfileEntity.id from PrescriptionEntity p where p.id in :ids")
    Set<Long> findProfileIds(@Param("ids") Set<Long> ids);
}
