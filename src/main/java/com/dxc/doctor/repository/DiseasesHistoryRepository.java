package com.dxc.doctor.repository;

import com.dxc.doctor.entity.DiseasesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface DiseasesHistoryRepository extends JpaRepository<DiseasesHistory, Long> {

    @Query("select d.medical_treatment_profile from DiseasesHistory d where d.name = :name")
    HashSet<Long> getProfileIdsByDisease(String disease);
}