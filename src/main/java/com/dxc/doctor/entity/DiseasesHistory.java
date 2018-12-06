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

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DiseasesHistory{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                '}';
    }
}
