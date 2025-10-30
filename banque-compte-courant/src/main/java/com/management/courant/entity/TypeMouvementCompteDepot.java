package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "type_mouvement_compte_depot", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "type_mouvement_compte_depot_libelle_key", columnNames = {"libelle"})
})
public class TypeMouvementCompteDepot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "libelle", nullable = false, length = 50)
    private String libelle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}