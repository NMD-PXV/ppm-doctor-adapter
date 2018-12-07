package com.dxc.doctor.service;

import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.*;
import com.dxc.doctor.exception.MedicalProfilesException;
import com.dxc.doctor.repository.*;
import com.dxc.doctor.util.Converter;
import com.dxc.doctor.util.ProfileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.dxc.doctor.common.MedicalProfilesStorageError.INVALID_INPUT_PROFILES;
import static com.dxc.doctor.common.MedicalProfilesStorageError.PATIENT_ID_IS_NULL_OR_CONTAINS_SPACE;
import static com.dxc.doctor.common.MedicalProfilesStorageError.PROFILES_NOT_FOUND;

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
        StringBuffer result = new StringBuffer();
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
            result.append("New id profile(s):\n");
            addProfiles(patientId, profiles, result);
        } else
            updateProfile(patientId, profiles, medicalProfileExistedList, result);
        return result.toString();
    }

    @Transactional
    public String addProfiles(String patientId, List<MedicalTreatmentProfile> profiles, StringBuffer result) {
        for (MedicalTreatmentProfile profileMapper : profiles) {
            MedicalTreatmentProfileEntity medicalProfileEntity = ProfileUtil.profile2entity(profileMapper);
            medicalProfileEntity.setPatientId(patientId);
            // save new profile to database
            medicalProfileRepository.saveAndFlush(medicalProfileEntity);
            result.append("ProfileId: ");
            result.append(medicalProfileEntity.getId());
            result.append("\n");
        }
        return result.toString();
    }

    @Transactional
    public String updateProfile(String patientId, List<MedicalTreatmentProfile> profiles,
                                List<MedicalTreatmentProfileEntity> medicalProfileExistedList,
                                StringBuffer result) {
        List<MedicalTreatmentProfile> medicalTreatmentProfileList = new ArrayList<>();
        /**
         * Run every profile then check that the input profile existed or not
         * if profile existed then update that profile
         * else add profile in to a list and after the loop end, create new profiles in list
         */
        //TODO BUG BUGGGGGGGGGGGGGGGGGGG
        for (int i = 0; i < profiles.size(); i++)
            if (medicalProfileExistedList.get(i).getId().equals(profiles.get(i).getId().longValue())) {
                //  Update MedicalTreatment Fields
                medicalProfileExistedList.get(i).setId(profiles.get(i).getId().longValue());
                medicalProfileExistedList.get(i).setDoctorUpdated(profiles.get(i).getDoctorUpdated());
                medicalProfileExistedList.get(i).setModifiedDate(new Date());

                // update diseases history
                List<String> diseasesMapper = profiles.get(i).getDiseasesHistory();
                List<DiseasesHistory> diseasesHistoryList = diseasesMapper.stream().
                        map(DiseasesHistory::new).
                        collect(Collectors.toList());
                medicalProfileExistedList.get(i).setDiseasesHistory(diseasesHistoryList);

                // Update GivenMedicine
                List<GivenMedicineEntity> givenMedicinesBeingUsed = givenMedicineRepository.getMedicinesByType(
                        medicalProfileExistedList.get(i).getPrescription().getId(), Type.BEING_USED.toString());
                List<GivenMedicineEntity> beingUsedMapperList = Converter.convertMedicinesForUpdate(
                        profiles.get(i).getPrescription().getBeingUsed(), Type.BEING_USED.toString());
                List<GivenMedicineEntity> beingUsedMedicines = updateGivenMedicines(givenMedicinesBeingUsed, beingUsedMapperList);

                List<GivenMedicineEntity> givenMedicinesRecentlyUsed = givenMedicineRepository.getMedicinesByType(
                        medicalProfileExistedList.get(i).getPrescription().getId(), Type.RECENTLY_USED.toString());
                List<GivenMedicineEntity> recentlyUsedMapperList = Converter.convertMedicinesForUpdate(
                        profiles.get(i).getPrescription().getRecentlyUsed(), Type.RECENTLY_USED.toString());
                List<GivenMedicineEntity> recentlyUsedMedicines = updateGivenMedicines(givenMedicinesRecentlyUsed, recentlyUsedMapperList);

                List<GivenMedicineEntity> medicinesUpdated = new ArrayList<>();
                medicinesUpdated.addAll(beingUsedMedicines);
                medicinesUpdated.addAll(recentlyUsedMedicines);

                //  Update Prescription
                PrescriptionEntity prescriptionEntity = prescriptionRepository
                        .findByIdEquals(medicalProfileExistedList.get(i).getPrescription().getId());
                prescriptionEntity.setGivenMedicines(medicinesUpdated);
                medicalProfileExistedList.get(i).setPrescription(prescriptionEntity);

                //  Update MedicalTestResult
                MedicalTestResultEntity medicalTestResultEntity = medicalTestRepository
                        .findByIdEquals(medicalProfileExistedList.get(i).getMedicalTestResult().getId());
                medicalProfileExistedList.get(i).setMedicalTestResult(Converter
                        .convertMedicalTestResultForUpdate(medicalTestResultEntity, profiles.get(i)));

                // save update to database
                medicalProfileRepository.save(medicalProfileExistedList.get(i));
                result.append("Updated: ");
                result.append("\n");
                result.append(patientId);
                result.append("\n");
            } else
                medicalTreatmentProfileList.add(profiles.get(i));
        if (medicalProfileExistedList.isEmpty())
            return result.toString();
        return addProfiles(patientId, medicalTreatmentProfileList, result);
    }

    @Transactional
    private List<GivenMedicineEntity> updateGivenMedicines(List<GivenMedicineEntity> medicinesExisted
            , List<GivenMedicineEntity> medicinesMapper) {
        List<GivenMedicineEntity> newMedicines = new ArrayList<>();
        if (medicinesExisted.size() <= medicinesMapper.size()) {
            for (GivenMedicineEntity medicineMapper : medicinesMapper) {
                for (GivenMedicineEntity medicineExisted : medicinesExisted) {
                    if (medicineExisted.getId().equals(medicineMapper.getId())) {
                        GivenMedicineEntity g = givenMedicineRepository.getMedicineById(medicineExisted.getId(),
                                medicineExisted.getType());
                        g.setId(medicineMapper.getId());
                        g.setName(medicineMapper.getName());
                        g.setQuantity(medicineMapper.getQuantity());
                        g.setType(medicineMapper.getType());
                        givenMedicineRepository.save(g);
                    } else {
                        GivenMedicineEntity newMedicine = new GivenMedicineEntity();
                        newMedicine.setName(medicineMapper.getName());
                        newMedicine.setQuantity(medicineMapper.getQuantity());
                        newMedicine.setType(medicineMapper.getType());
                        newMedicine.setType(medicineMapper.getType());
                        newMedicine.setDeleted(false);
                        newMedicines.add(newMedicine);
                    }
                }
            }
            medicinesExisted.addAll(newMedicines);
        } else {
            for (GivenMedicineEntity medicineExisted : medicinesExisted) {
                for (GivenMedicineEntity medicineMapper : medicinesMapper) {
                    GivenMedicineEntity g = givenMedicineRepository.getMedicineById(medicineMapper.getId(),
                            medicineMapper.getType());
                    if (medicineExisted.getId().equals(medicineMapper.getId())) {
                        g.setId(medicineMapper.getId());
                        g.setName(medicineMapper.getName());
                        g.setQuantity(medicineMapper.getQuantity());
                        g.setType(medicineMapper.getType());
                        givenMedicineRepository.save(g);
                    } else {
                        medicineExisted.setDeleted(true);
                        givenMedicineRepository.save(g);
                    }
                }
            }
        }
        if (newMedicines != null)
            medicinesExisted.addAll(newMedicines);
        return medicinesExisted;
    }

    public List<MedicalTreatmentProfile> searchTreatmentProfiles(String name, String disease, String medicine) {
        if (name == null && disease == null && medicine == null)
            return medicalProfileRepository.findAll().
                    stream().
                    map(ProfileUtil::entity2Profile).
                    collect(Collectors.toList());
        if (name != null)
            return searchProfilesByPatientId(name);

        if (disease != null && medicine != null) {
            Set<Long> idsFromDisease = diseasesHistoryRepository.getProfileIdsByDisease(disease);
            Set<Long> idsFromMedicine = givenMedicineRepository.getPrescriptionIdsByName(medicine);
            idsFromMedicine.retainAll(idsFromDisease);

            if (idsFromMedicine.isEmpty())
                throw new MedicalProfilesException(PROFILES_NOT_FOUND);
            return medicalProfileRepository.findMultiProfiles(idsFromMedicine).
                    stream().
                    map(ProfileUtil::entity2Profile).
                    collect(Collectors.toList());
        }

        Set<Long> ids = null;
        if (disease != null)
            ids = diseasesHistoryRepository.getProfileIdsByDisease(disease);
        else if (medicine != null)
            ids = givenMedicineRepository.getPrescriptionIdsByName(medicine);

        if (ids.isEmpty())
            throw new MedicalProfilesException(PROFILES_NOT_FOUND);
        return medicalProfileRepository.findMultiProfiles(ids).
                stream().
                map(ProfileUtil::entity2Profile).
                collect(Collectors.toList());
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

