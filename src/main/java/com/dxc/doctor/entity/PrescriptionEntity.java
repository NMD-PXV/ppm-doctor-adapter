package com.dxc.doctor.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Prescription")
public class PrescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @OneToOne(optional=false, mappedBy = "prescription")
    private MedicalTreatmentProfileEntity medicalTreatmentProfileEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PRESCRIPTION_ID")
    private List<GivenMedicineEntity> givenMedicines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MedicalTreatmentProfileEntity getMedicalTreatmentProfileEntity() {
        return medicalTreatmentProfileEntity;
    }

    public void setMedicalTreatmentProfileEntity(MedicalTreatmentProfileEntity medicalTreatmentProfileEntity) {
        this.medicalTreatmentProfileEntity = medicalTreatmentProfileEntity;
    }

    public List<GivenMedicineEntity> getGivenMedicines() {
        return givenMedicines;
    }

    public void setGivenMedicines(List<GivenMedicineEntity> givenMedicines) {
        this.givenMedicines = givenMedicines;
    }


}
