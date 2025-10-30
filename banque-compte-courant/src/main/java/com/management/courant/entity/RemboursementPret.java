package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "remboursement_pret", schema = "public")
public class RemboursementPret implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "montant_rembourse", nullable = false, precision = 15, scale = 2)
    private BigDecimal montantRembourse;

    @Column(name = "interet_paye", nullable = false, precision = 15, scale = 2)
    private BigDecimal interetPaye;

    @Column(name = "date_paiement", nullable = false)
    private Instant datePaiement;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_mouvement_compte_courant", nullable = false)
    private MouvementCompteCourant idMouvementCompteCourant;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_pret", nullable = false)
    private Pret idPret;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMontantRembourse() {
        return montantRembourse;
    }

    public void setMontantRembourse(BigDecimal montantRembourse) {
        this.montantRembourse = montantRembourse;
    }

    public BigDecimal getInteretPaye() {
        return interetPaye;
    }

    public void setInteretPaye(BigDecimal interetPaye) {
        this.interetPaye = interetPaye;
    }

    public Instant getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public MouvementCompteCourant getIdMouvementCompteCourant() {
        return idMouvementCompteCourant;
    }

    public void setIdMouvementCompteCourant(MouvementCompteCourant idMouvementCompteCourant) {
        this.idMouvementCompteCourant = idMouvementCompteCourant;
    }

    public Pret getIdPret() {
        return idPret;
    }

    public void setIdPret(Pret idPret) {
        this.idPret = idPret;
    }

}