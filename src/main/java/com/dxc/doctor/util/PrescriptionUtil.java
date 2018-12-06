package com.dxc.doctor.util;

import com.dxc.doctor.api.model.Prescription;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.GivenMedicineEntity;
import com.dxc.doctor.entity.PrescriptionEntity;

import java.util.ArrayList;
import java.util.List;
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

    public static PrescriptionEntity prescription2Entity(Prescription prescription) {
        PrescriptionEntity entity = new PrescriptionEntity();
        List<GivenMedicineEntity> beingUsed = GivenMedicineUtil
                .givenMedicineToEntity(prescription.getBeingUsed(), Type.BEING_USED.toString());
        List<GivenMedicineEntity> recentlyUsed = GivenMedicineUtil
                .givenMedicineToEntity(prescription.getRecentlyUsed(), Type.RECENTLY_USED.toString());
        List<GivenMedicineEntity> medicines = new ArrayList<>();
        medicines.addAll(beingUsed);
        medicines.addAll(recentlyUsed);
        entity.setGivenMedicines(medicines);
        return entity;
    }
}
