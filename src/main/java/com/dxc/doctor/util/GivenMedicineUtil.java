package com.dxc.doctor.util;

import com.dxc.doctor.api.model.GivenMedicine;
import com.dxc.doctor.common.Type;
import com.dxc.doctor.entity.GivenMedicineEntity;
import com.dxc.doctor.repository.GivenMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GivenMedicineUtil {



    public static GivenMedicine entity2GivenMedicine(GivenMedicineEntity entity) {
        GivenMedicine givenMedicine = new GivenMedicine();
        givenMedicine.setId(new BigDecimal(entity.getId()));
        givenMedicine.setName(entity.getName());
        givenMedicine.setQuantity(entity.getQuantity());
        return givenMedicine;
    }

    public static GivenMedicineEntity givenMedicineToEntity(GivenMedicine givenMedicines, String type) {
        return GivenMedicineEntity.builder()
                .name(givenMedicines.getName())
                .quantity(givenMedicines.getQuantity())
                .type(type)
                .deleted(false).build();
    }

    public static List<GivenMedicineEntity> updateMedicine(
            List<GivenMedicineEntity> medicinesExisted, List<GivenMedicineEntity> medicinesMapped
    ,GivenMedicineRepository givenMedicineRepository) {
        List<GivenMedicineEntity> newMedicines = new ArrayList<>();
        if (medicinesExisted.size() <= medicinesMapped.size()) {
            for (GivenMedicineEntity medicineMapper : medicinesMapped) {
                for (GivenMedicineEntity medicineExisted : medicinesExisted) {
                    if (medicineExisted.getId().equals(medicineMapper.getId())) {
                        GivenMedicineEntity g = givenMedicineRepository.getMedicineById(medicineExisted.getId(),
                                medicineExisted.getType());
                        g.setId(medicineMapper.getId());
                        g.setName(medicineMapper.getName());
                        g.setQuantity(medicineMapper.getQuantity());
                        g.setType(medicineMapper.getType());
                        givenMedicineRepository.save(g);
                    } else {
                        GivenMedicineEntity newMedicine = new GivenMedicineEntity();
                        newMedicine.setName(medicineMapper.getName());
                        newMedicine.setQuantity(medicineMapper.getQuantity());
                        newMedicine.setType(medicineMapper.getType());
                        newMedicine.setType(medicineMapper.getType());
                        newMedicine.setDeleted(false);
                        newMedicines.add(newMedicine);
                    }
                }
            }
            medicinesExisted.addAll(newMedicines);
        } else {
            for (GivenMedicineEntity medicineExisted : medicinesExisted) {
                for (GivenMedicineEntity medicineMapper : medicinesMapped) {
                    GivenMedicineEntity g = givenMedicineRepository.getMedicineById(medicineMapper.getId(),
                            medicineMapper.getType());
                    if (medicineExisted.getId().equals(medicineMapper.getId())) {
                        g.setId(medicineMapper.getId());
                        g.setName(medicineMapper.getName());
                        g.setQuantity(medicineMapper.getQuantity());
                        g.setType(medicineMapper.getType());
                        givenMedicineRepository.save(g);
                    } else {
                        medicineExisted.setDeleted(true);
                        givenMedicineRepository.save(g);
                    }
                }
            }
        }
        if (newMedicines != null)
            medicinesExisted.addAll(newMedicines);
        return medicinesExisted;
    }
}
