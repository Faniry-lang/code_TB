package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "compte_courant", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "compte_courant_numero_compte_key", columnNames = {"numero_compte"}),
        @UniqueConstraint(name = "compte_courant_id_client_key", columnNames = {"id_client"})
})
public class CompteCourant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "numero_compte", nullable = false, length = 50)
    private String numeroCompte;

    @Column(name = "solde", nullable = false, precision = 15, scale = 2)
    private BigDecimal solde;

    @Column(name = "date_ouverture", nullable = false)
    private Instant dateOuverture;

    @Column(name = "date_fermeture")
    private Instant dateFermeture;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_statut", nullable = false)
    private StatutCompte idStatut;

    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_client", nullable = false)
    private Client idClient;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public Instant getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Instant getDateFermeture() {
        return dateFermeture;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public StatutCompte getIdStatut() {
        return idStatut;
    }

    public void setIdStatut(StatutCompte idStatut) {
        this.idStatut = idStatut;
    }

    public Client getIdClient() {
        return idClient;
    }

    public void setIdClient(Client idClient) {
        this.idClient = idClient;
    }

}