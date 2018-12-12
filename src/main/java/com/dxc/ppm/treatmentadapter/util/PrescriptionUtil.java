package com.dxc.ppm.treatmentadapter.util;

import com.dxc.ppm.treatmentadapter.api.model.Prescription;
import com.dxc.ppm.treatmentadapter.common.Type;
import com.dxc.ppm.treatmentadapter.entity.GivenMedicineEntity;
import com.dxc.ppm.treatmentadapter.entity.PrescriptionEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionUtil {
    public static Prescription entity2Prescription(PrescriptionEntity entity){
        Prescription prescription = new Prescription();
        prescription.setId(new BigDecimal(entity.getId()));
        prescription.setBeingUsed(entity.getGivenMedicines().
                stream().
                filter(g -> g.getType().equals(Type.BEING_USED.toString())).
                map(GivenMedicineUtil::entity2GivenMedicine).
                collect(Collectors.toList()));

        prescription.setRecentlyUsed(entity.getGivenMedicines().
                stream().
                filter(g -> g.getType().equals(Type.RECENTLY_USED.toString())).
                map(GivenMedicineUtil::entity2GivenMedicine).
                collect(Collectors.toList()));
        return prescription;
    }

    public static PrescriptionEntity prescription2Entity(Prescription prescription) {
        PrescriptionEntity entity = new PrescriptionEntity();
        List<GivenMedicineEntity> medicines = new ArrayList<>();
        medicines.addAll(prescription.getBeingUsed().stream().
                map(g -> GivenMedicineUtil.givenMedicineToEntity(g, Type.BEING_USED.toString())).
                collect(Collectors.toList()));
        medicines.addAll(prescription.getRecentlyUsed().stream().
                map(g -> GivenMedicineUtil.givenMedicineToEntity(g, Type.RECENTLY_USED.toString())).
                collect(Collectors.toList()));
        entity.setGivenMedicines(medicines);
        return entity;
    }
}
