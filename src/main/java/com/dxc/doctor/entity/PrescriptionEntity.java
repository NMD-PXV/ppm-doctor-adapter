package com.dxc.doctor.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Prescription")
@Getter
@Setter
@Builder
public class PrescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @OneToOne(optional = false, mappedBy = "prescription")
    private MedicalTreatmentProfileEntity medicalTreatmentProfileEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PRESCRIPTION_ID")
    private List<GivenMedicineEntity> givenMedicines;

    public PrescriptionEntity() {
    }

    public PrescriptionEntity(Long id, MedicalTreatmentProfileEntity medicalTreatmentProfileEntity,
                              List<GivenMedicineEntity> givenMedicines) {
        this.id = id;
        this.medicalTreatmentProfileEntity = medicalTreatmentProfileEntity;
        this.givenMedicines = givenMedicines;
    }
}
