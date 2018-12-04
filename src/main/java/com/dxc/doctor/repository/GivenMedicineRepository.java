package com.dxc.doctor.repository;

import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.entity.GivenMedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GivenMedicineRepository extends JpaRepository<GivenMedicineEntity, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM GivenMedicineEntity medicine WHERE prescription_id = :prescriptionId")
    void deleteMedicinesByPrescriptionId(@Param("prescriptionId") Long prescriptionId);
}
