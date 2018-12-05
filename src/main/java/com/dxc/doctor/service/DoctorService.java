package com.dxc.doctor.service;

import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.GivenMedicineEntity;
import com.dxc.doctor.entity.MedicalTestResultEntity;
import com.dxc.doctor.entity.MedicalTreatmentProfileEntity;
import com.dxc.doctor.entity.PrescriptionEntity;
import com.dxc.doctor.repository.GivenMedicineRepository;
import com.dxc.doctor.repository.MedicalProfileRepository;
import com.dxc.doctor.repository.MedicalTestRepository;
import com.dxc.doctor.repository.PrescriptionRepository;
import com.dxc.doctor.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {


    private MedicalProfileRepository medicalProfileRepository;
    private GivenMedicineRepository givenMedicineRepository;
    private PrescriptionRepository prescriptionRepository;
    private MedicalTestRepository medicalTestRepository;

    @Autowired
    public DoctorService(MedicalProfileRepository medicalProfileRepository,
                         GivenMedicineRepository givenMedicineRepository,
                         PrescriptionRepository prescriptionRepository,
                         MedicalTestRepository medicalTestRepository) {
        this.medicalProfileRepository = medicalProfileRepository;
        this.givenMedicineRepository = givenMedicineRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.medicalTestRepository = medicalTestRepository;
    }

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
            addProfiles(id, profiles, result);
        } else {
            updateProfile(id, profiles, medicalProfileExistedList, result);
        }
        return result.toString();
    }

    public String updateProfile(String id, List<MedicalTreatmentProfile> profiles,
                                List<MedicalTreatmentProfileEntity> medicalProfileExistedList,
                                StringBuffer result) {
        List<MedicalTreatmentProfile> medicalTreatmentProfileList = new ArrayList<>();
        /**
         * Run every profile then check that the input profile existed or not
         * if profile existed then update that profile
         * else add profile in to a list and after the loop end, create new profiles in list
         */
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getProfileId().equals(medicalProfileExistedList.get(i).getProfileId())) {
                /**
                 *  Update MedicalTreatment Fields
                 */
                medicalProfileExistedList.get(i).setDoctorUpdated(profiles.get(i).getDoctorUpdated());
                //medicalProfileExistedList.get(i).setDiseasesHistory(profiles.get(i).getDiseasesHistory().toString());
                medicalProfileExistedList.get(i).setModifiedDate(new Date());

                /**
                 *  Update Prescription
                 */
                PrescriptionEntity prescriptionEntity = prescriptionRepository
                        .findByIdEquals(medicalProfileExistedList.get(i).getPrescription().getId());
                givenMedicineRepository.deleteMedicinesByPrescriptionId(prescriptionEntity.getId());

                /**
                 *  Update GivenMedicine
                 */
                List<GivenMedicineEntity> beingUsedMapperList = Converter.convertGivenMedicineToEntity(
                        profiles.get(i).getPrescription().getBeingUsed(), Type.BEING_USED.toString());
                List<GivenMedicineEntity> recentlyUsedMapperList = Converter.convertGivenMedicineToEntity(
                        profiles.get(i).getPrescription().getRecentlyUsed(), Type.RECENTLY_USED.toString());

                List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
                givenMedicineEntityList.addAll(beingUsedMapperList);
                givenMedicineEntityList.addAll(recentlyUsedMapperList);

                prescriptionEntity.setGivenMedicines(givenMedicineEntityList);
                medicalProfileExistedList.get(i).setPrescription(prescriptionEntity);

                /**
                 *  Update MedicalTestResult
                 */
                MedicalTestResultEntity medicalTestResultEntity = medicalTestRepository
                        .findByIdEquals(medicalProfileExistedList.get(i).getMedicalTestResult().getId());
                medicalProfileExistedList.get(i).setMedicalTestResult(Converter.updateMedicalTestResult(
                        medicalTestResultEntity, profiles.get(i)));
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
        }
        if (medicalProfileExistedList.size() == 0) {
            return result.toString();
        }
        return addProfiles(id, medicalTreatmentProfileList, result);
    }

    public String addProfiles(String id, List<MedicalTreatmentProfile> profiles, StringBuffer result) {
        for (MedicalTreatmentProfile profileMapper : profiles) {

            /**
             * set profile info
             */
            String profileId = UUID.randomUUID().toString();
            MedicalTreatmentProfileEntity medicalTreatmentProfileEntity = new MedicalTreatmentProfileEntity();
            medicalTreatmentProfileEntity.setDoctor(profileMapper.getDoctor());
            medicalTreatmentProfileEntity.setDoctorUpdated(profileMapper.getDoctor());
           // medicalTreatmentProfileEntity.setDiseasesHistory(profileMapper.getDiseasesHistory().toString());
            medicalTreatmentProfileEntity.setCreateDate(new Date());
            medicalTreatmentProfileEntity.setModifiedDate(new Date());
            medicalTreatmentProfileEntity.setProfileId(profileId);
            medicalTreatmentProfileEntity.setPatientId(id);

            /**
             * set medical Test result
             */
            medicalTreatmentProfileEntity.setMedicalTestResult(
                    Converter.convertMedicalTestResultToEntity(profileMapper));

            /**
             * set Given Medicine
             */
            List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
            givenMedicineEntityList.addAll(Converter.convertGivenMedicineToEntity(profileMapper
                    .getPrescription()
                    .getBeingUsed(), Type.BEING_USED.toString()));
            givenMedicineEntityList.addAll(Converter.convertGivenMedicineToEntity(profileMapper
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

    public List<MedicalTreatmentProfile> searchTreatmentProfiles(String name, String disease, String medicine) {

        return null;
    }
}