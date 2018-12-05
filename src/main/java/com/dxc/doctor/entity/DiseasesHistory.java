package com.dxc.doctor.entity;

import javax.persistence.*;


@Entity
@Table(name = "DiseasesHistory")
public class DiseasesHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long Id;

    @Column(name = "NAME")
    private String name;
}
