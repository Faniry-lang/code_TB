package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "direction", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "direction_libelle_key", columnNames = {"libelle"})
})
public class Direction implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "niveau", nullable = false)
    private Integer niveau;

    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}