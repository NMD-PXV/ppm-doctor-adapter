package com.dxc.ppm.treatmentadapter.delegate;

import com.dxc.ppm.treatmentadapter.api.V1ApiDelegate;
import com.dxc.ppm.treatmentadapter.api.model.MedicalTreatmentProfile;
import com.dxc.ppm.treatmentadapter.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class V1ApiDelegateImp implements V1ApiDelegate {

    @Autowired
    private DoctorService doctorService;


    @Override
    public ResponseEntity<List<MedicalTreatmentProfile>> searchPatients(List<String> ids, String disease, String medicine) {
        return ResponseEntity.ok(doctorService.searchTreatmentProfiles(ids, disease, medicine));
    }

    @Override
    public ResponseEntity<List<MedicalTreatmentProfile>> searchProfilesByPatientId(String id) {
        return ResponseEntity.ok(doctorService.searchProfilesByPatientId(id));
    }

    @Override
    public ResponseEntity<String> searchTest(String id, String name) {
        return ResponseEntity.ok(doctorService.searchTest(id, name));
    }

    @Override
    public ResponseEntity<String> upsertProfiles(String id, List<MedicalTreatmentProfile> profiles) {
        return ResponseEntity.ok(doctorService.upsertProfiles(id, profiles));
    }
}
