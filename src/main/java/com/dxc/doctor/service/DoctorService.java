package com.dxc.doctor.service;

import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.*;
import com.dxc.doctor.repository.*;
import com.dxc.doctor.util.Converter;
import com.dxc.doctor.util.ProfileUtil;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private DiseasesHistoryRepository diseasesHistoryRepository;
    private MedicalProfileRepository medicalProfileRepository;
    private GivenMedicineRepository givenMedicineRepository;
    private PrescriptionRepository prescriptionRepository;
    private MedicalTestRepository medicalTestRepository;

    @Autowired
    public DoctorService(DiseasesHistoryRepository diseasesHistoryRepository,
                         MedicalProfileRepository medicalProfileRepository,
                         GivenMedicineRepository givenMedicineRepository,
                         PrescriptionRepository prescriptionRepository,
                         MedicalTestRepository medicalTestRepository) {
        this.diseasesHistoryRepository = diseasesHistoryRepository;
        this.medicalProfileRepository = medicalProfileRepository;
        this.givenMedicineRepository = givenMedicineRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.medicalTestRepository = medicalTestRepository;
    }

    @Transactional
    public String upsertProfiles(String id, List<MedicalTreatmentProfile> profiles) {
        StringBuffer result = new StringBuffer();

        //TODO check id;
        if (id == null) {
            //TODO throw exception;
        }

        //TODO check profiles
        /**
         *  check the profiles of patient existed or not
         *  if the profiles existed then updateProfile
         *  else create profiles
         */
        List<MedicalTreatmentProfileEntity> medicalProfileExistedList =
                medicalProfileRepository.findByPatientIdEquals(id);
        if (medicalProfileExistedList.size() == 0) {
            result.append("New id profile(s):\n");
            addProfiles(id, profiles, result); }
            else {
               updateProfile(id, profiles, medicalProfileExistedList, result);
            }
        return result.toString();
    }



    @Transactional
    public String updateProfile(String id, List<MedicalTreatmentProfile> profiles,
                                List<MedicalTreatmentProfileEntity> medicalProfileExistedList,
                                StringBuffer result) {
        List<MedicalTreatmentProfile> medicalTreatmentProfileList = new ArrayList<>();
        /**
         * Run every profile then check that the input profile existed or not
         * if profile existed then update that profile
         * else add profile in to a list and after the loop end, create new profiles in list
         */
        for (int i = 0; i < profiles.size(); i++)
            if (profiles.get(i).getProfileId().equals(medicalProfileExistedList.get(i).getProfileId())) {
                /**
                 *  Update MedicalTreatment Fields
                 */
                medicalProfileExistedList.get(i).setDoctorUpdated(profiles.get(i).getDoctorUpdated());
                medicalProfileExistedList.get(i).setModifiedDate(new Date());

                /**
                 * update diseases history
                 */
                List<DiseasesHistory> diseasesHistoryList = new ArrayList<>();
                List<String> diseasesMapper = profiles.get(i).getDiseasesHistory();
                diseasesHistoryList = diseasesMapper.stream().map(d -> {
                    DiseasesHistory disease = new DiseasesHistory();
                    disease.setName(d);
                    return disease;
                }).collect(Collectors.toList());
                medicalProfileExistedList.get(i).setDiseasesHistory(diseasesHistoryList);


                /**
                 *  Update GivenMedicine
                 */
                List<GivenMedicineEntity> givenMedicinesBeingUsed = givenMedicineRepository.getMedicinesByType(
                        medicalProfileExistedList.get(i).getPrescription().getId(), Type.BEING_USED.toString());
                List<GivenMedicineEntity> beingUsedMapperList = Converter.convertMedicinesForUpdate(
                        profiles.get(i).getPrescription().getBeingUsed(), Type.BEING_USED.toString());
                List<GivenMedicineEntity> beingUsedMedicines = updateGivenMedicine(givenMedicinesBeingUsed, beingUsedMapperList);

                List<GivenMedicineEntity> givenMedicinesRecentlyUsed = givenMedicineRepository.getMedicinesByType(
                        medicalProfileExistedList.get(i).getPrescription().getId(), Type.RECENTLY_USED.toString());
                List<GivenMedicineEntity> recentlyUsedMapperList = Converter.convertMedicinesForUpdate(
                        profiles.get(i).getPrescription().getRecentlyUsed(), Type.RECENTLY_USED.toString());
                List<GivenMedicineEntity> recentlyUsedMedicines = updateGivenMedicine(givenMedicinesRecentlyUsed, recentlyUsedMapperList);

                List<GivenMedicineEntity> medicinesUpdated = new ArrayList<>();
                medicinesUpdated.addAll(beingUsedMedicines);
                medicinesUpdated.addAll(recentlyUsedMedicines);
                /**
                 *  Update Prescription
                 */
                PrescriptionEntity prescriptionEntity = prescriptionRepository
                        .findByIdEquals(medicalProfileExistedList.get(i).getPrescription().getId());
                prescriptionEntity.setGivenMedicines(medicinesUpdated);
                medicalProfileExistedList.get(i).setPrescription(prescriptionEntity);
                /**
                 *  Update MedicalTestResult
                 */
                MedicalTestResultEntity medicalTestResultEntity = medicalTestRepository
                        .findByIdEquals(medicalProfileExistedList.get(i).getMedicalTestResult().getId());
                medicalProfileExistedList.get(i).setMedicalTestResult(Converter
                        .convertMedicalTestResultForUpdate(medicalTestResultEntity, profiles.get(i)));
                /**
                 * save update to database
                 */
                medicalProfileRepository.save(medicalProfileExistedList.get(i));
                result.append("Updated: ");
                result.append("\n");
                result.append(id);
                result.append("\n");
            } else {
                medicalTreatmentProfileList.add(profiles.get(i));
            }
        if (medicalProfileExistedList.size() == 0) {
            return result.toString();
        }
        return addProfiles(id, medicalTreatmentProfileList, result);
    }

    @Transactional
    public String addProfiles(String id, List<MedicalTreatmentProfile> profiles, StringBuffer result) {
        for (MedicalTreatmentProfile profileMapper : profiles) {
            /**
             * set profile info
             */
            String profileId = UUID.randomUUID().toString();
            MedicalTreatmentProfileEntity medicalTreatmentProfileEntity = new MedicalTreatmentProfileEntity();
            medicalTreatmentProfileEntity.setDoctor(profileMapper.getDoctor());
            medicalTreatmentProfileEntity.setDoctorUpdated(profileMapper.getDoctor());
            medicalTreatmentProfileEntity.setCreateDate(new Date());
            medicalTreatmentProfileEntity.setModifiedDate(new Date());
            medicalTreatmentProfileEntity.setProfileId(profileId);
            medicalTreatmentProfileEntity.setPatientId(id);
            /**
             * set diseases history
             */
            List<DiseasesHistory> diseasesEntityList = profileMapper.getDiseasesHistory().stream().map(d -> {
                DiseasesHistory diseasesHistory = new DiseasesHistory();
                diseasesHistory.setName(d);
                return diseasesHistory;
            }).collect(Collectors.toList());
            medicalTreatmentProfileEntity.setDiseasesHistory(diseasesEntityList);
            /**
             * set medical Test result
             */
            medicalTreatmentProfileEntity.setMedicalTestResult(
                    Converter.convertMedicalTestResultToEntity(profileMapper));
            /**
             * set Given Medicine
             */
            List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
            givenMedicineEntityList.addAll(Converter.convertMedicinesToEntity(profileMapper
                    .getPrescription()
                    .getBeingUsed(), Type.BEING_USED.toString()));
            givenMedicineEntityList.addAll(Converter.convertMedicinesToEntity(profileMapper
                    .getPrescription()
                    .getBeingUsed(), Type.RECENTLY_USED.toString()));
            /**
             * set Given Prescription
             */
            PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
            prescriptionEntity.setGivenMedicines(givenMedicineEntityList);
            medicalTreatmentProfileEntity.setPrescription(prescriptionEntity);
            /**
             * save new profile to database
             */
            medicalProfileRepository.saveAndFlush(medicalTreatmentProfileEntity);
            result.append("Created: ");
            result.append("\n");
            result.append(profileId);
            result.append("\n");
        }
        return result.toString();
    }


    @Transactional
    private List<GivenMedicineEntity> updateGivenMedicine(List<GivenMedicineEntity> medicinesExsited
            , List<GivenMedicineEntity> medicinesMapper) {
        List<GivenMedicineEntity> newMedicines = new ArrayList<>();
        if (medicinesExsited.size() <= medicinesMapper.size()) {
            for (GivenMedicineEntity medicineMapper : medicinesMapper) {
                for (GivenMedicineEntity medicineExisted : medicinesExsited) {
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
            medicinesExsited.addAll(newMedicines);
        } else {
            for (GivenMedicineEntity medicineExisted : medicinesExsited) {
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
        if (newMedicines != null) {
            medicinesExsited.addAll(newMedicines);
        }
        return medicinesExsited;
    }

    public List<MedicalTreatmentProfile> searchTreatmentProfiles(String name, String disease, String medicine) {
        Set<Long> ids = diseasesHistoryRepository.getProfileIdsByDisease(disease);
        System.out.println(ids);
//        return medicalProfileRepository.findMultiProfiles(ids).
//                stream().
//                map(ProfileUtil::entity2Profile).
//                collect(Collectors.toList());
        return null;
    }

    public List<MedicalTreatmentProfile> searchProfilesByPatientId(String id) {
        List<MedicalTreatmentProfileEntity> profilesEntity = medicalProfileRepository.findByPatientIdEquals(id);
        List<MedicalTreatmentProfile> profiles = profilesEntity.stream().map(e -> {
            MedicalTreatmentProfile profile = new MedicalTreatmentProfile();
            BigDecimal bigDecimalId = new BigDecimal(e.getId());
            profile = ProfileUtil.entity2Profile(e);
            return profile;
        }).collect(Collectors.toList());
        return profiles;
    }
}

