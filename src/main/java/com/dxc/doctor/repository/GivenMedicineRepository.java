package com.dxc.doctor.repository;

import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.entity.GivenMedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface GivenMedicineRepository extends JpaRepository<GivenMedicineEntity, Long> {

    @Query(value = "SELECT medicine FROM GivenMedicineEntity medicine WHERE prescription_id = :prescriptionId")
    List<GivenMedicineEntity> findGivenMedicineByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    @Query(value = "SELECT medicine FROM GivenMedicineEntity medicine WHERE medicine.type = :type AND " +
            "medicine.deleted = false")
    List<GivenMedicineEntity> findGivenMedicineByType(@Param("type") String type);
}
