package com.dxc.doctor.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MedicalTreatmentProfile")
@Getter
@Setter
@Builder
public class MedicalTreatmentProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PATIENT_ID")
    private String patientId;

    @Column(name = "DOCTOR")
    private String doctor;

    @Column(name = "DOCTOR_UPDATED")
    private String doctorUpdated;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEDICAL_TEST_RESULT_ID")
    private MedicalTestResultEntity medicalTestResult;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PRESCRIPTION_ID")
    private PrescriptionEntity prescription;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEDICAL_TREATMENT_PROFILE_ID")
    private List<DiseasesHistory> diseasesHistory;

    public MedicalTreatmentProfileEntity() {
    }

    public MedicalTreatmentProfileEntity(Long id, String patientId, String doctor, String doctorUpdated,
                                         Date createDate, Date modifiedDate, MedicalTestResultEntity medicalTestResult,
                                         PrescriptionEntity prescription, List<DiseasesHistory> diseasesHistory) {
        this.id = id;
        this.patientId = patientId;
        this.doctor = doctor;
        this.doctorUpdated = doctorUpdated;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.medicalTestResult = medicalTestResult;
        this.prescription = prescription;
        this.diseasesHistory = diseasesHistory;
    }
}
