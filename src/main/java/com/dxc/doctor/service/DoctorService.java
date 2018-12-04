package com.dxc.doctor.service;

import com.dxc.doctor.api.model.GivenMedicine;
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

    @Autowired
    private MedicalProfileRepository medicalProfileRepository;

    @Autowired
    private GivenMedicineRepository givenMedicineRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MedicalTestRepository medicalTestRepository;

    public String upsertProfiles(String id, List<MedicalTreatmentProfile> profiles) {
        StringBuffer result = new StringBuffer();

        //TODO check id;
        if (id == null) {
            //TODO throw exception;
        }

        //TODO check profiles
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
                                List<MedicalTreatmentProfileEntity> medicalProfileExistedList, StringBuffer result) {
        List<MedicalTreatmentProfile> medicalTreatmentProfileList = new ArrayList<>();

        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getProfileId().equals(medicalProfileExistedList.get(i).getProfileId())) {

                /**
                 *  Update MedicalTreatment Fields
                 */
                medicalProfileExistedList.get(i).setDoctorUpdated(profiles.get(i).getDoctorUpdated());
                medicalProfileExistedList.get(i).setDiseasesHistory(profiles.get(i).getDiseasesHistory().toString());
                medicalProfileExistedList.get(i).setModifiedDate(new Date());

                /**
                 *  Update GivenMedicine
                 */
                List<GivenMedicineEntity> begingUsedEntityList =
                        givenMedicineRepository.findGivenMedicineByType(Type.BEING_USED.toString());
                List<GivenMedicineEntity> beingUsedMapperList = Converter.convertGivenMedicineToEntity(
                        profiles.get(i).getPrescription().getBeingUsed(), Type.BEING_USED.toString());

                List<GivenMedicineEntity> recentlyUsedEntityList =
                        givenMedicineRepository.findGivenMedicineByType(Type.RECENTLY_USED.toString());
                List<GivenMedicineEntity> recentlyUsedMapperList = Converter.convertGivenMedicineToEntity(
                        profiles.get(i).getPrescription().getBeingUsed(), Type.RECENTLY_USED.toString());



                List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
                givenMedicineEntityList.addAll(updateGivenMedicine(begingUsedEntityList, beingUsedMapperList, profiles, i));
                givenMedicineEntityList.addAll(updateGivenMedicine(recentlyUsedEntityList, recentlyUsedMapperList, profiles, i));

                /**
                 *  Update MedicalTestResult
                 */
                MedicalTestResultEntity medicalTestResultEntity = medicalTestRepository
                        .findByIdEquals(medicalProfileExistedList.get(i).getMedicalTestResult().getId());
                medicalProfileExistedList.get(i).setMedicalTestResult(Converter.updateMedicalTestResult(
                        medicalTestResultEntity, profiles.get(i)));

                /**
                 *  Update Prescription
                 */
                PrescriptionEntity prescriptionEntity = prescriptionRepository
                        .findByIdEquals(medicalProfileExistedList.get(i).getPrescription().getId());

                prescriptionEntity.setGivenMedicines(givenMedicineEntityList);
                medicalProfileExistedList.get(i).setPrescription(prescriptionEntity);

                /**
                 * ------------
                 */
                medicalProfileRepository.save(medicalProfileExistedList.get(i));
                result.append("Updated: ");
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

    private List<GivenMedicineEntity> updateGivenMedicine(List<GivenMedicineEntity> usedEntityList,
                                                          List<GivenMedicineEntity> usedEntityMapperList,
                                                          List<MedicalTreatmentProfile> profiles,
                                                          int i) {
        /**
         * compare medicine list in database with list of input medicine;
         */
        if (usedEntityList.size() <= profiles.get(i).getPrescription().getBeingUsed().size()) {
            for (int j = 0; j < usedEntityMapperList.size(); j++) {
                GivenMedicineEntity res = compare(usedEntityMapperList.get(i), usedEntityList);
                if (res != null)
                    usedEntityList.add(res);
                else {
                    usedEntityList.get(i).setName(usedEntityMapperList.get(i).getName());
                    usedEntityList.get(i).setType(usedEntityMapperList.get(i).getType());
                    usedEntityList.get(i).setQuantity(usedEntityMapperList.get(i).getQuantity());
                }
            }

        } else {
            for (int j = 0; j < usedEntityList.size(); j++) {
                GivenMedicineEntity res = compare(usedEntityMapperList.get(i), usedEntityList);
                if (res != null)
                    usedEntityList.get(i).setDeleted(true);
                else {
                    usedEntityList.get(i).setName(usedEntityMapperList.get(i).getName());
                    usedEntityList.get(i).setType(usedEntityMapperList.get(i).getType());
                    usedEntityList.get(i).setQuantity(usedEntityMapperList.get(i).getQuantity());
                }
            }
            return usedEntityList;
        }
        return null;
    }


    public String addProfiles(String id, List<MedicalTreatmentProfile> profiles, StringBuffer result) {
        for (MedicalTreatmentProfile profileMapper : profiles) {
            String profileId = UUID.randomUUID().toString();
            MedicalTreatmentProfileEntity medicalTreatmentProfileEntity = new MedicalTreatmentProfileEntity();
            medicalTreatmentProfileEntity.setDoctor(profileMapper.getDoctor());
            medicalTreatmentProfileEntity.setDoctorUpdated(profileMapper.getDoctor());
            medicalTreatmentProfileEntity.setDiseasesHistory(profileMapper.getDiseasesHistory().toString());
            medicalTreatmentProfileEntity.setCreateDate(new Date());
            medicalTreatmentProfileEntity.setModifiedDate(new Date());
            medicalTreatmentProfileEntity.setProfileId(profileId);
            medicalTreatmentProfileEntity.setPatientId(id);

            medicalTreatmentProfileEntity.setMedicalTestResult(
                    Converter.convertMedicalTestResultToEntity(profileMapper));

            List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
            givenMedicineEntityList.addAll(Converter.convertGivenMedicineToEntity(profileMapper
                    .getPrescription()
                    .getBeingUsed(), Type.BEING_USED.toString()));
            givenMedicineEntityList.addAll(Converter.convertGivenMedicineToEntity(profileMapper
                    .getPrescription()
                    .getBeingUsed(), Type.RECENTLY_USED.toString()));

            PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
            prescriptionEntity.setGivenMedicines(givenMedicineEntityList);

            medicalTreatmentProfileEntity.setPrescription(prescriptionEntity);

            medicalProfileRepository.saveAndFlush(medicalTreatmentProfileEntity);

            result.append("Created: ");
            result.append(profileId);
            result.append("\n");
        }
        return result.toString();
    }

    public GivenMedicineEntity compare(GivenMedicineEntity givenMedicine,
                                       List<GivenMedicineEntity> givenMedicines) {
        if (!givenMedicines.contains(givenMedicine))
            return givenMedicine;
        return null;
    }
}
