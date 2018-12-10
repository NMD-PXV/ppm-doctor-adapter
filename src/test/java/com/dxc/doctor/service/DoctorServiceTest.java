package com.dxc.doctor.service;


import com.dxc.doctor.api.model.MedicalTestResult;
import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.api.model.Prescription;
import com.dxc.doctor.delegate.V1ApiDelegateImp;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private MedicalTreatmentProfile medicalTreatmentProfile;

    @Mock
    private MedicalTestResult medicalTestResult;

    @Mock
    private Prescription prescription;

    private static final List<String> DISEASEHISTORY = new ArrayList<>();
    private static final List<String> ALLERGICMEDICINES = new ArrayList<>();

//    @Before
//    public void setUp{
//        DISEASEHISTORY.add("FLU");
//        DISEASEHISTORY.add("H1N1");
//
//        ALLERGICMEDICINES.add("Panadol 500");
//        ALLERGICMEDICINES.add("Afferagant");
//        medicalTestResult.setXRay("Normal");
//        medicalTestResult.setBloodType("O");
//        medicalTestResult.setUltraSound("Normal");
//        medicalTestResult.setAllergicMedicines(ALLERGICMEDICINES);
//
//        prescription.setBeingUsed();
//
//        medicalTreatmentProfile.setCreatedDate(new LocalDate());
//        medicalTreatmentProfile.setModifiedDate(new LocalDate());
//        medicalTreatmentProfile.setDoctor("Dr Strange");
//        medicalTreatmentProfile.setDoctorUpdated("Dr Strange");
//
//        medicalTreatmentProfile.setDiseasesHistory(DISEASEHISTORY);
//        medicalTreatmentProfile.setMedicalTestResult(medicalTestResult);
//        medicalTreatmentProfile.setPrescription();
//    }
//
//    private static String PATIENT_ID = "100";
//
//    private static final List<String> INPUT_PROFILES = ;
//    private static final String CREATED_PROFILE_RESULTS = "100";
//
//
//    @Test
//    public void upsertProfiles() {
//        when(doctorService.addProfiles(PATIENT_ID, INPUT_PROFILES)).thenReturn(CREATED_PROFILE_RESULTS);
//    }
}