package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "historique_statut_pret", schema = "public")
public class HistoriqueStatutPret implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date_modification", nullable = false)
    private Instant dateModification;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_statut", nullable = false)
    private StatutPret idStatut;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_pret", nullable = false)
    private Pret idPret;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDateModification() {
        return dateModification;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public StatutPret getIdStatut() {
        return idStatut;
    }

    public void setIdStatut(StatutPret idStatut) {
        this.idStatut = idStatut;
    }

    public Pret getIdPret() {
        return idPret;
    }

    public void setIdPret(Pret idPret) {
        this.idPret = idPret;
    }

}