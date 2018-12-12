package com.dxc.ppm.treatmentadapter.entity;

import javax.persistence.*;

@Entity
@Table(name = "MedicalTestResult")
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

    @OneToOne(optional=false, mappedBy = "medicalTestResult")
    private MedicalTreatmentProfileEntity medicalTreatmentProfileEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getxRay() {
        return xRay;
    }

    public void setxRay(String xRay) {
        this.xRay = xRay;
    }

    public String getUltraSound() {
        return ultraSound;
    }

    public void setUltraSound(String ultraSound) {
        this.ultraSound = ultraSound;
    }

    public String getAllergicMedicines() {
        return allergicMedicines;
    }

    public void setAllergicMedicines(String allergicMedicines) {
        this.allergicMedicines = allergicMedicines;
    }

    public MedicalTreatmentProfileEntity getMedicalTreatmentProfileEntity() {
        return medicalTreatmentProfileEntity;
    }

    public void setMedicalTreatmentProfileEntity(MedicalTreatmentProfileEntity medicalTreatmentProfileEntity) {
        this.medicalTreatmentProfileEntity = medicalTreatmentProfileEntity;
    }

    public MedicalTestResultEntity() {
    }
}
