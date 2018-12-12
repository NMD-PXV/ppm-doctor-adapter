package com.dxc.ppm.treatmentadapter.service;

import com.dxc.ppm.treatmentadapter.api.model.MedicalTreatmentProfile;
import com.dxc.ppm.treatmentadapter.common.Type;
import com.dxc.ppm.treatmentadapter.entity.GivenMedicineEntity;
import com.dxc.ppm.treatmentadapter.entity.MedicalTestResultEntity;
import com.dxc.ppm.treatmentadapter.entity.MedicalTreatmentProfileEntity;
import com.dxc.ppm.treatmentadapter.entity.PrescriptionEntity;
import com.dxc.ppm.treatmentadapter.exception.MedicalProfilesException;
import com.dxc.ppm.treatmentadapter.util.Converter;
import com.dxc.ppm.treatmentadapter.util.GivenMedicineUtil;
import com.dxc.ppm.treatmentadapter.util.ProfileUtil;
import com.dxc.ppm.treatmentadapter.util.TestResultUtil;
import com.dxc.ppm.treatmentadapter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.dxc.ppm.treatmentadapter.common.MedicalProfilesStorageError.INVALID_INPUT_PROFILES;
import static com.dxc.ppm.treatmentadapter.common.MedicalProfilesStorageError.PATIENT_ID_IS_NULL_OR_CONTAINS_SPACE;
import static com.dxc.ppm.treatmentadapter.common.MedicalProfilesStorageError.PROFILES_NOT_FOUND;

@Service
public class DoctorService {

    @Autowired
    private DiseasesHistoryRepository diseasesHistoryRepository;
    @Autowired
    private MedicalProfileRepository medicalProfileRepository;
    @Autowired
    private GivenMedicineRepository givenMedicineRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private MedicalTestRepository medicalTestRepository;


    @Transactional
    public String upsertProfiles(String patientId, List<MedicalTreatmentProfile> profiles) {
        if (patientId == null || patientId.contains(" "))
            throw new MedicalProfilesException(PATIENT_ID_IS_NULL_OR_CONTAINS_SPACE, patientId);
        if (profiles.isEmpty())
            throw new MedicalProfilesException(INVALID_INPUT_PROFILES, profiles);
        /**
         *  check the profiles of patient existed or not
         *  if the profiles existed then updateProfile
         *  else create profiles
         */
        List<MedicalTreatmentProfileEntity> medicalProfileExistedList =
                medicalProfileRepository.findByPatientIdEquals(patientId);
        if (medicalProfileExistedList.isEmpty()) {
            addProfiles(patientId, profiles);
        } else
            updateProfile(patientId, profiles);
        return patientId;
    }

    @Transactional
    public String addProfiles(String patientId, List<MedicalTreatmentProfile> profiles) {
        for (MedicalTreatmentProfile profile : profiles) {
            MedicalTreatmentProfileEntity medicalProfileEntity = ProfileUtil.profile2entity(profile);
            medicalProfileEntity.setPatientId(patientId);
            // save new profile to database
            medicalProfileRepository.saveAndFlush(medicalProfileEntity);
        }
        return patientId;
    }

    @Transactional
    public void updateProfile(String patientId, List<MedicalTreatmentProfile> profiles) {
        /**
         * Run every profile then check that the input profile existed or not
         * if profile existed then update that profile
         * else add profile in to a list and after the loop end, create new profiles in list
         */
        List<MedicalTreatmentProfile> newProfiles = new ArrayList<>();
        for (MedicalTreatmentProfile inputProfile : profiles) {
            MedicalTreatmentProfileEntity existedProfile = medicalProfileRepository
                    .findByIdEquals(inputProfile.getId().longValue());
            if (existedProfile == null) {
                newProfiles.add(inputProfile);
            } else {
                ProfileUtil.updateProfile(existedProfile, inputProfile);

                // Update medicines

                List<GivenMedicineEntity> mBeingUsedExisted = givenMedicineRepository.getMedicinesByType(
                        existedProfile.getPrescription().getId(), Type.BEING_USED.toString());
                List<GivenMedicineEntity> mBeingMapped = Converter.convertMedicines2Entities(
                        inputProfile.getPrescription().getBeingUsed(), Type.BEING_USED.toString());
                List<GivenMedicineEntity> updatedMedicines = new ArrayList<>(GivenMedicineUtil.updateMedicine(mBeingUsedExisted, mBeingMapped, givenMedicineRepository));
                List<GivenMedicineEntity> mRecentlyExisted = givenMedicineRepository.getMedicinesByType(
                        existedProfile.getPrescription().getId(), Type.RECENTLY_USED.toString());
                List<GivenMedicineEntity> mRecentlyMapped = Converter.convertMedicines2Entities(
                        inputProfile.getPrescription().getRecentlyUsed(), Type.RECENTLY_USED.toString());
                updatedMedicines.addAll(GivenMedicineUtil.updateMedicine(mRecentlyExisted, mRecentlyMapped, givenMedicineRepository));

                //  Update Prescription
                PrescriptionEntity prescriptionEntity = prescriptionRepository
                        .findByIdEquals(existedProfile.getPrescription().getId());
                existedProfile.setPrescription(prescriptionEntity);

                //  Update MedicalTestResult
                MedicalTestResultEntity medicalTestResultEntity = TestResultUtil.
                        updateMedicalResult(inputProfile.getMedicalTestResult());
                existedProfile.setMedicalTestResult(medicalTestResultEntity);

                // save update to database
                medicalProfileRepository.save(existedProfile);
            }
        }
        if (!newProfiles.isEmpty())
            addProfiles(patientId, newProfiles);
    }

    public List<MedicalTreatmentProfile> searchTreatmentProfiles(List<String> ids, String disease, String medicine) {
        if (ids == null && disease == null && medicine == null)
            return medicalProfileRepository.findAll().
                    stream().
                    map(ProfileUtil::entity2Profile).
                    collect(Collectors.toList());
        Set<Long> retIds;

        if (ids != null) {
            retIds = medicalProfileRepository.findProfileIdsByPatientId(ids);
            if (disease != null) {
                Set<Long> idsFromDisease = diseasesHistoryRepository.getProfileIdsByDisease(disease);
                if (medicine != null) {
                    Set<Long> prescriptionIds = givenMedicineRepository.getPrescriptionIdsByName(medicine);
                    Set<Long> idsFromMedicine = prescriptionRepository.findProfileIds(prescriptionIds);
                    idsFromMedicine.retainAll(idsFromDisease);
                    retIds.retainAll(idsFromMedicine);
                } else
                    retIds.retainAll(idsFromDisease);

            } else if (medicine != null) {
                Set<Long> prescriptionIds = givenMedicineRepository.getPrescriptionIdsByName(medicine);
                Set<Long> idsFromMedicine = prescriptionRepository.findProfileIds(prescriptionIds);
                retIds.retainAll(idsFromMedicine);
            }
        } else {
            if (disease != null) {
                Set<Long> idsFromDisease = diseasesHistoryRepository.getProfileIdsByDisease(disease);
                if (medicine != null) {
                    Set<Long> prescriptionIds = givenMedicineRepository.getPrescriptionIdsByName(medicine);
                    Set<Long> idsFromMedicine = prescriptionRepository.findProfileIds(prescriptionIds);
                    idsFromMedicine.retainAll(idsFromDisease);
                    retIds = idsFromMedicine;
                } else
                    retIds = idsFromDisease;
            } else {
                Set<Long> prescriptionIds = givenMedicineRepository.getPrescriptionIdsByName(medicine);
                retIds = prescriptionRepository.findProfileIds(prescriptionIds);
            }
        }
        if (retIds.isEmpty())
            throw new MedicalProfilesException(PROFILES_NOT_FOUND);
        return medicalProfileRepository.findMultiProfilesById(retIds)
                .stream()
                .map(ProfileUtil::entity2Profile)
                .collect(Collectors.toList());
    }


    public List<MedicalTreatmentProfile> searchProfilesByPatientId(String id) {
        if (id == null || id.contains(" "))
            throw new MedicalProfilesException(PATIENT_ID_IS_NULL_OR_CONTAINS_SPACE, id);
        List<MedicalTreatmentProfileEntity> profilesEntity = medicalProfileRepository.findByPatientIdEquals(id);

        if (profilesEntity.isEmpty())
            throw new MedicalProfilesException(PROFILES_NOT_FOUND);
        return profilesEntity.stream().
                map(ProfileUtil::entity2Profile).
                collect(Collectors.toList());
    }

    public String searchTest(String id, String name) {
        if (id == null || id.contains(" "))
            throw new MedicalProfilesException(PATIENT_ID_IS_NULL_OR_CONTAINS_SPACE, id);
        List<MedicalTreatmentProfileEntity> profiles = medicalProfileRepository.findByPatientIdEquals(id);
        if (profiles.isEmpty())
            throw new MedicalProfilesException(PROFILES_NOT_FOUND);
        for (MedicalTreatmentProfileEntity p : profiles) {
            Long idMedicalTest = p.getMedicalTestResult().getId();
            String allergicMedicines = medicalTestRepository.getProfileIdsByDisease(idMedicalTest);
            if (Arrays.stream(allergicMedicines.split(",")).collect(Collectors.toList()).contains(name))
                return "found allergicMedicine";
        }
        return "Not found allergicMedicine";
    }
}
