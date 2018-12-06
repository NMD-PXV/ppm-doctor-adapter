package com.dxc.doctor.util;

import com.dxc.doctor.api.model.Prescription;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.PrescriptionEntity;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static com.dxc.doctor.common.Type.*;

public class PrescriptionUtil {
    public static Prescription entity2Prescription(PrescriptionEntity entity){
        Prescription prescription = new Prescription();
        prescription.setId(Converter.convertBigDecimalToLong(entity.getId()));
        prescription.setBeingUsed(entity.getGivenMedicines().
                stream().
                filter(g -> g.getType().equals(BEING_USED.toString())).
                map(GivenMedicineUtil::entity2GivenMedicine).
                collect(Collectors.toList()));

        prescription.setRecentlyUsed(entity.getGivenMedicines().
                stream().
                filter(g -> g.getType().equals(RECENTLY_USED.toString())).
                map(GivenMedicineUtil::entity2GivenMedicine).
                collect(Collectors.toList()));
        return prescription;
    }
}
