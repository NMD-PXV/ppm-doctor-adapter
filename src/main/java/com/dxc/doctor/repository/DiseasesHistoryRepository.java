package com.dxc.doctor.repository;

import com.dxc.doctor.entity.DiseasesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DiseasesHistoryRepository extends JpaRepository<DiseasesHistory, Long> {

    @Query("select d.medicalTreatmentProfile.id from DiseasesHistory d where d.name = :name")
    Set<Long> getProfileIdsByDisease(@Param("name") String name);
}