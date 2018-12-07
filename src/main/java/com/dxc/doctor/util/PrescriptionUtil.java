package com.dxc.doctor.util;

import com.dxc.doctor.api.model.Prescription;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.GivenMedicineEntity;
import com.dxc.doctor.entity.PrescriptionEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static com.dxc.doctor.common.Type.*;

public class PrescriptionUtil {
    public static Prescription entity2Prescription(PrescriptionEntity entity){
        Prescription prescription = new Prescription();
        prescription.setId(new BigDecimal(entity.getId()));
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
