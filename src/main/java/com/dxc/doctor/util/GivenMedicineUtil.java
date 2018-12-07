package com.dxc.doctor.util;

import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.GivenMedicineEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GivenMedicineUtil {
    protected static GivenMedicine entity2GivenMedicine(GivenMedicineEntity entity){
        GivenMedicine givenMedicine = new GivenMedicine();
        givenMedicine.setId(new BigDecimal(entity.getId()));
        givenMedicine.setName(entity.getName());
        givenMedicine.setQuantity(entity.getQuantity());
        return givenMedicine;
    }

    protected static GivenMedicineEntity givenMedicineToEntity(GivenMedicine givenMedicines, String type) {
        GivenMedicineEntity entities = new GivenMedicineEntity();
        entities.setName(givenMedicines.getName());
        entities.setQuantity(givenMedicines.getQuantity());
        entities.setType(type);
        entities.setDeleted(false);
        return entities;
    }
}
