package com.dxc.doctor.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "DiseasesHistory")
@Getter
@Setter
@Builder
public class DiseasesHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long Id;

    @Column(name = "NAME")
    private String name;


    @ManyToOne
    private MedicalTreatmentProfileEntity medicalTreatmentProfile;


    @Override
    public String toString() {
        return "DiseasesHistory{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                '}';
    }

    public DiseasesHistory() {
    }

    public DiseasesHistory(Long id, String name, MedicalTreatmentProfileEntity medicalTreatmentProfile) {
        Id = id;
        this.name = name;
        this.medicalTreatmentProfile = medicalTreatmentProfile;
    }
}
