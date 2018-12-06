package com.dxc.doctor.repository;

import com.dxc.doctor.entity.DiseasesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public interface DiseasesHistoryRepository extends JpaRepository<DiseasesHistory, Long> {

    @Query("select d from DiseasesHistory d where d.name = :name")
    List<DiseasesHistory> getProfileIdsByDisease(@Param("name") String disease);
}