package com.dxc.doctor.util;

import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.entity.GivenMedicineEntity;

public class GivenMedicineUtil {
    protected static GivenMedicine entity2GivenMedicine(GivenMedicineEntity entity){
        GivenMedicine givenMedicine = new GivenMedicine();
        givenMedicine.setName(entity.getName());
        givenMedicine.setQuantity(entity.getQuantity());
        return givenMedicine;
    }
}
