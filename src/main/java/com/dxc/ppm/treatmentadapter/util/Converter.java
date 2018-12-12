package com.dxc.ppm.treatmentadapter.util;


import com.dxc.ppm.treatmentadapter.api.model.GivenMedicine;
import com.dxc.ppm.treatmentadapter.entity.GivenMedicineEntity;

import java.util.ArrayList;
import java.util.List;

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
