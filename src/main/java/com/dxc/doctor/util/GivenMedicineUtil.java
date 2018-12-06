package com.dxc.doctor.util;

import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.GivenMedicineEntity;

import java.util.ArrayList;
import java.util.List;

public class GivenMedicineUtil {
    protected static GivenMedicine entity2GivenMedicine(GivenMedicineEntity entity){
        GivenMedicine givenMedicine = new GivenMedicine();
        givenMedicine.setId(Converter.convertBigDecimalToLong(entity.getId()));
        givenMedicine.setName(entity.getName());
        givenMedicine.setQuantity(entity.getQuantity());
        return givenMedicine;
    }

    protected static List<GivenMedicineEntity> givenMedicineToEntity(List<GivenMedicine> givenMedicines,
                                                             String type) {
        List<GivenMedicineEntity> entities = new ArrayList<>();
        givenMedicines.stream().forEach(givenMedicineMapper -> {
            GivenMedicineEntity givenMedicineEntity = new GivenMedicineEntity();
            givenMedicineEntity.setName(givenMedicineMapper.getName());
            givenMedicineEntity.setQuantity(givenMedicineMapper.getQuantity());
            givenMedicineEntity.setType(type);
            givenMedicineEntity.setDeleted(false);
            entities.add(givenMedicineEntity);
        });
        return entities;
    }
}
