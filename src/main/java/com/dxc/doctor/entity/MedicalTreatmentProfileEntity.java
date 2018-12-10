package com.dxc.doctor.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MedicalTreatmentProfile")
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

    public List<DiseasesHistory> getDiseasesHistory() {
        return diseasesHistory;
    }

    public void setDiseasesHistory(List<DiseasesHistory> diseasesHistory) {
        this.diseasesHistory = diseasesHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public MedicalTestResultEntity getMedicalTestResult() {
        return medicalTestResult;
    }

    public void setMedicalTestResult(MedicalTestResultEntity medicalTestResult) {
        this.medicalTestResult = medicalTestResult;
    }

    public PrescriptionEntity getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionEntity prescription) {
        this.prescription = prescription;
    }

    public String getDoctorUpdated() {
        return doctorUpdated;
    }

    public void setDoctorUpdated(String doctorUpdated) {
        this.doctorUpdated = doctorUpdated;
    }

    public MedicalTreatmentProfileEntity() {
    }
}
