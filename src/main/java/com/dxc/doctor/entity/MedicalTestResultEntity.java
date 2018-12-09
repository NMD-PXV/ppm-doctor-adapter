package com.dxc.doctor.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "MedicalTestResult")
@Getter
@Setter
@Builder
public class MedicalTestResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "BLOOD_TYPE")
    private String bloodType;

    @Column(name = "X_RAY")
    private String xRay;

    @Column(name = "ULTRA_SOUND")
    private String ultraSound;

    @Column(name = "ALLERGIC_MEDICINES")
    private String allergicMedicines;

    @OneToOne(optional = false, mappedBy = "medicalTestResult")
    private MedicalTreatmentProfileEntity medicalTreatmentProfileEntity;

    public MedicalTestResultEntity() {
    }

    public MedicalTestResultEntity(Long id, String bloodType, String xRay, String ultraSound,
                                   String allergicMedicines,
                                   MedicalTreatmentProfileEntity medicalTreatmentProfileEntity) {
        this.id = id;
        this.bloodType = bloodType;
        this.xRay = xRay;
        this.ultraSound = ultraSound;
        this.allergicMedicines = allergicMedicines;
        this.medicalTreatmentProfileEntity = medicalTreatmentProfileEntity;
    }
}
