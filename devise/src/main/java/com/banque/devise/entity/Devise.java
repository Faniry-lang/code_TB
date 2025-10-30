package com.banque.devise.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Devise implements Serializable {
    private static final long serialVersionUID = 1L;

    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal valeur;

    // Constructeurs
    public Devise() {}

    public Devise(String libelle, LocalDate dateDebut, LocalDate dateFin, BigDecimal valeur) {
        this.libelle = libelle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.valeur = valeur;
    }

    // Getters et Setters
    public String getLibelle() {return libelle;}
    public void setLibelle(String libelle) {this.libelle = libelle;}
    public LocalDate getDateDebut() {return dateDebut;}
    public void setDateDebut(LocalDate dateDebut) {this.dateDebut = dateDebut;}
    public BigDecimal getValeur() {return valeur;}
    public void setValeur(BigDecimal valeur) {this.valeur = valeur;}
    public LocalDate getDateFin() {return dateFin;}
    public void setDateFin(LocalDate dateFin) {this.dateFin = dateFin;}

    // equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Devise devise = (Devise) o;
        return Objects.equals(libelle, devise.libelle) &&
                Objects.equals(dateDebut, devise.dateDebut) &&
                Objects.equals(dateFin, devise.dateFin) &&
                Objects.equals(valeur, devise.valeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(libelle, dateDebut, dateFin, valeur);
    }

    @Override
    public String toString() {
        return "Devise{" +
                "libelle='" + libelle + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", valeur=" + valeur +
                '}';
    }
}