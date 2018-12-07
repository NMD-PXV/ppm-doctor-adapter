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

    protected static GivenMedicineEntity givenMedicineToEntity(GivenMedicine givenMedicines,
                                                             String type) {
        GivenMedicineEntity entities = new GivenMedicineEntity();
        entities.setName(entities.getName());
        entities.setQuantity(entities.getQuantity());
        entities.setType(type);
        entities.setDeleted(false);
        return entities;
    }
}
