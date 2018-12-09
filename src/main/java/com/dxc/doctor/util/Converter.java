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
            givenMedicineEntityList.add(GivenMedicineEntity.builder()
                    .id(givenMedicineMapper.getId().longValue())
                    .deleted(false)
                    .name(givenMedicineMapper.getName())
                    .quantity(givenMedicineMapper.getQuantity())
                    .type(type).build());
        });
        return givenMedicineEntityList;
    }
}
