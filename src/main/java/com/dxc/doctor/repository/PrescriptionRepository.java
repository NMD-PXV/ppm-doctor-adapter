package com.dxc.doctor.repository;

import com.dxc.doctor.entity.GivenMedicineEntity;
import com.dxc.doctor.entity.PrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, Long> {
    PrescriptionEntity findByIdEquals(Long prescriptionId);
}
