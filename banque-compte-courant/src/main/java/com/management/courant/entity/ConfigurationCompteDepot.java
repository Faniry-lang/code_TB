package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "configuration_compte_depot", schema = "public")
public class ConfigurationCompteDepot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "taux_interet_annuel", nullable = false)
    private Double tauxInteretAnnuel;

    @Column(name = "limite_retrait_mensuel", nullable = false)
    private Integer limiteRetraitMensuel;

    @Column(name = "pourcentage_max_retrait", nullable = false)
    private Double pourcentageMaxRetrait;

    @Column(name = "date_application", nullable = false)
    private Instant dateApplication;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_compte_depot", nullable = false)
    private CompteDepot idCompteDepot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTauxInteretAnnuel() {
        return tauxInteretAnnuel;
    }

    public void setTauxInteretAnnuel(Double tauxInteretAnnuel) {
        this.tauxInteretAnnuel = tauxInteretAnnuel;
    }

    public Integer getLimiteRetraitMensuel() {
        return limiteRetraitMensuel;
    }

    public void setLimiteRetraitMensuel(Integer limiteRetraitMensuel) {
        this.limiteRetraitMensuel = limiteRetraitMensuel;
    }

    public Double getPourcentageMaxRetrait() {
        return pourcentageMaxRetrait;
    }

    public void setPourcentageMaxRetrait(Double pourcentageMaxRetrait) {
        this.pourcentageMaxRetrait = pourcentageMaxRetrait;
    }

    public Instant getDateApplication() {
        return dateApplication;
    }

    public void setDateApplication(Instant dateApplication) {
        this.dateApplication = dateApplication;
    }

    public CompteDepot getIdCompteDepot() {
        return idCompteDepot;
    }

    public void setIdCompteDepot(CompteDepot idCompteDepot) {
        this.idCompteDepot = idCompteDepot;
    }

}