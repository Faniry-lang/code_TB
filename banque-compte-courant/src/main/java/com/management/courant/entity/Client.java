package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "client", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "client_matricule_key", columnNames = {"matricule"})
})
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "matricule", nullable = false, length = 50)
    private String matricule;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "telephone", nullable = false, length = 50)
    private String telephone;

    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @OneToOne(mappedBy = "idClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private CompteCourant compteCourant;

    @OneToOne(mappedBy = "idClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private CompteDepot compteDepot;

    @OneToMany(mappedBy = "idClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pret> prets = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public CompteCourant getCompteCourant() {
        return compteCourant;
    }

    public void setCompteCourant(CompteCourant compteCourant) {
        this.compteCourant = compteCourant;
    }

    public CompteDepot getCompteDepot() {
        return compteDepot;
    }

    public void setCompteDepot(CompteDepot compteDepot) {
        this.compteDepot = compteDepot;
    }

    public Set<Pret> getPrets() {
        return prets;
    }

    public void setPrets(Set<Pret> prets) {
        this.prets = prets;
    }

}