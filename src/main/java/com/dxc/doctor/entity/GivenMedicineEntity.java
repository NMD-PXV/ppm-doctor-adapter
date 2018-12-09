package com.dxc.doctor.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "GivenMedicine")
@Getter
@Setter
@Builder
public class GivenMedicineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DELETED")
    private boolean deleted;

    @Column(name = "NAME")
    private String name;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "TYPE")
    private String type;

    @ManyToOne
    private PrescriptionEntity prescription;

    public GivenMedicineEntity() {
    }

    public GivenMedicineEntity(Long id, boolean deleted, String name, int quantity, String type,
                               PrescriptionEntity prescription) {
        this.id = id;
        this.deleted = deleted;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.prescription = prescription;
    }
}
