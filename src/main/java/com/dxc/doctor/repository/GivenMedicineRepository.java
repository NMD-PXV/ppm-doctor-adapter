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

    @Query("SELECT medicines FROM GivenMedicineEntity medicines WHERE prescription_id = :prescriptionId AND " +
            "deleted = false")
    List<GivenMedicineEntity> getMedicinesByPreId(@Param("prescriptionId") Long prescriptionId);

    @Query("SELECT medicines FROM GivenMedicineEntity medicines WHERE prescription_id = :prescriptionId AND " +
            "deleted = false AND type = :type")
    List<GivenMedicineEntity> getMedicinesByType(@Param("prescriptionId") Long prescriptionId,
                                                     @Param("type") String type);

    @Query("SELECT medicines FROM GivenMedicineEntity medicines WHERE id = :id AND " +
            "deleted = false AND type = :type")
    GivenMedicineEntity getMedicineById(@Param("id") Long id,
                                                 @Param("type") String type);


//    List<GivenMedicineEntity> findByPrescriptionIdAndDeleted(Long prescriptionId, boolean deleted);
}
