package com.dxc.ppm.treatmentadapter.service;


import com.dxc.ppm.treatmentadapter.api.model.GivenMedicine;
import com.dxc.ppm.treatmentadapter.api.model.MedicalTestResult;
import com.dxc.ppm.treatmentadapter.api.model.MedicalTreatmentProfile;
import com.dxc.ppm.treatmentadapter.api.model.Prescription;
import com.dxc.ppm.treatmentadapter.entity.*;
import com.dxc.ppm.treatmentadapter.util.*;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private MedicalTreatmentProfile medicalTreatmentProfile;

    @Mock
    private MedicalTestResult medicalTestResult;

    @Mock
    private Prescription prescription;

    @Mock
    private GivenMedicine givenMedicine;

    @Mock
    private MedicalTreatmentProfileEntity medicalTreatmentProfileEntityl;

    @Mock
    private MedicalTestResultEntity medicalTestResultEntity;

    @Mock
    private GivenMedicineEntity givenMedicineEntity;

    @Mock
    private DiseasesHistory diseasesHistory;

    @Mock
    private PrescriptionEntity prescriptionEntity;

    @Mock
    private TestResultUtil testResultUtil;

    @Mock
    private ProfileUtil profileUtil;

    @Mock
    private PrescriptionUtil prescriptionUtil;

    @Mock
    private GivenMedicineUtil givenMedicineUtil;

    @Mock
    private Converter converter;

    private static final List<String> DISEASES_HISTORY = new ArrayList<>();
    private static final List<String> ALLERGIC_MEDICINES = new ArrayList<>();
    private static final List<GivenMedicine> BEING_USED_MEDICINES = new ArrayList<>();
    private static final List<GivenMedicine> RECENTLY_USED_MEDICINES = new ArrayList<>();
    private static final List<MedicalTreatmentProfile> INPUT_PROFILES = new ArrayList<>();
    private static final String PATIENT_ID = "b889e160-d15f-4b19-af8d-c3f0603b6487";
    private static final String CREATED_PROFILE_RESULTS = "b889e160-d15f-4b19-af8d-c3f0603b6487";

    @Before
    public void setUp() {
        DISEASES_HISTORY.add("FLU");
        DISEASES_HISTORY.add("H1N1");
        medicalTreatmentProfile.setDiseasesHistory(DISEASES_HISTORY);

        givenMedicine.setName("Paracetamol 500");
        givenMedicine.setQuantity(15);
        BEING_USED_MEDICINES.add(givenMedicine);

        givenMedicine.setName("Paracetamol 800");
        givenMedicine.setQuantity(10);
        RECENTLY_USED_MEDICINES.add(givenMedicine);

        ALLERGIC_MEDICINES.add("Panadol 500");
        ALLERGIC_MEDICINES.add("Afferagant");
        medicalTestResult.setXRay("Normal");
        medicalTestResult.setBloodType("O");
        medicalTestResult.setUltraSound("Normal");
        medicalTestResult.setAllergicMedicines(ALLERGIC_MEDICINES);
        medicalTreatmentProfile.setMedicalTestResult(medicalTestResult);

        prescription.setBeingUsed(BEING_USED_MEDICINES);
        prescription.setRecentlyUsed(RECENTLY_USED_MEDICINES);
        medicalTreatmentProfile.setPrescription(prescription);

        medicalTreatmentProfile.setCreatedDate(new LocalDate());
        medicalTreatmentProfile.setModifiedDate(new LocalDate());
        medicalTreatmentProfile.setDoctor("Dr Strange");
        medicalTreatmentProfile.setDoctorUpdated("Dr Strange");
        medicalTreatmentProfile.setPatientId(PATIENT_ID);

        INPUT_PROFILES.add(medicalTreatmentProfile);

    }

    @Test
    public void upsertProfiles() {

//        doAnswer((String) invocation -> {
//
//        });
        when(doctorService.addProfiles(PATIENT_ID, INPUT_PROFILES)).thenReturn(CREATED_PROFILE_RESULTS);
        Assert.assertEquals(doctorService.addProfiles(PATIENT_ID, INPUT_PROFILES), CREATED_PROFILE_RESULTS);

    }
}