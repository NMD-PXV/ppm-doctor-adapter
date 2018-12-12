package com.dxc.ppm.treatmentadapter.repository;

import com.dxc.ppm.treatmentadapter.entity.GivenMedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GivenMedicineRepository extends JpaRepository<GivenMedicineEntity, Long> {

    @Query("SELECT medicines FROM GivenMedicineEntity medicines WHERE prescription_id = :prescriptionId AND " +
            "deleted = false AND type = :type")
    List<GivenMedicineEntity> getMedicinesByType(@Param("prescriptionId") Long prescriptionId,
                                                     @Param("type") String type);

    @Query("SELECT medicines FROM GivenMedicineEntity medicines WHERE id = :id AND " +
            "deleted = false AND type = :type")
    GivenMedicineEntity getMedicineById(@Param("id") Long id,
                                                 @Param("type") String type);

    @Query("select g.prescription.id from GivenMedicineEntity g where g.name = :name and g.deleted = false")
    Set<Long> getPrescriptionIdsByName(@Param("name") String name);


}
