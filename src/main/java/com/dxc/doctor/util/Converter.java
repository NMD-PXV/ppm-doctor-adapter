package com.dxc.doctor.util;


import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.api.model.MedicalTreatmentProfile;
import com.dxc.doctor.entity.GivenMedicineEntity;
import com.dxc.doctor.entity.MedicalTestResultEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {


    public static List<GivenMedicineEntity> convertMedicines2Entities(List<GivenMedicine> givenMedicines,
                                                                         String type) {
        List<GivenMedicineEntity> givenMedicineEntityList = new ArrayList<>();
        givenMedicines.stream().forEach(givenMedicineMapper -> {
            GivenMedicineEntity givenMedicineEntity = new GivenMedicineEntity();
            givenMedicineEntity.setId(givenMedicineMapper.getId().longValue());
            givenMedicineEntity.setName(givenMedicineMapper.getName());
            givenMedicineEntity.setQuantity(givenMedicineMapper.getQuantity());
            givenMedicineEntity.setType(type);
            givenMedicineEntity.setDeleted(false);
            givenMedicineEntityList.add(givenMedicineEntity);
        });
        return givenMedicineEntityList;
    }
}
