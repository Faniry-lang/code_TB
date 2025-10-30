package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pret", schema = "public")
public class Pret implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "montant_pret", nullable = false, precision = 15, scale = 2)
    private BigDecimal montantPret;

    @Column(name = "taux_interet_annuel", nullable = false, precision = 15, scale = 2)
    private BigDecimal tauxInteretAnnuel;

    @Column(name = "periodicite_remboursement", nullable = false)
    private Integer periodiciteRemboursement;

    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "date_fermeture")
    private Instant dateFermeture;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client idClient;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMontantPret() {
        return montantPret;
    }

    public void setMontantPret(BigDecimal montantPret) {
        this.montantPret = montantPret;
    }

    public BigDecimal getTauxInteretAnnuel() {
        return tauxInteretAnnuel;
    }

    public void setTauxInteretAnnuel(BigDecimal tauxInteretAnnuel) {
        this.tauxInteretAnnuel = tauxInteretAnnuel;
    }

    public Integer getPeriodiciteRemboursement() {
        return periodiciteRemboursement;
    }

    public void setPeriodiciteRemboursement(Integer periodiciteRemboursement) {
        this.periodiciteRemboursement = periodiciteRemboursement;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateFermeture() {
        return dateFermeture;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public Client getIdClient() {
        return idClient;
    }

    public void setIdClient(Client idClient) {
        this.idClient = idClient;
    }

}