package com.management.courant.remote;

import com.management.courant.entity.Client;
import jakarta.ejb.Remote;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Remote
public interface ClientRemote{
    List<Client> findAll();
    Client findById(int id);
    Client save(Client client);
    Client update(Client client);
    void delete(int id);

    // Ajout de la méthode de recherche par critères
    List<Client> findByCriteria(String nom, String prenom, String email);

    // Calcule le solde avec intérêts du compte dépôt (alias pour calculerSoldeReel)
    BigDecimal calculerSoldeAvecInteret(int idClient);

    // Calcule le solde total simplifié du client
    BigDecimal calculerSoldeTotale(int idClient);

    // Créer un client
    Client creerClient(
            String nom,
            String prenom,
            LocalDate dateNaissance,
            String adresse,
            String email,
            String telephone);

    Client updateClient(
            int id,
            String nom,
            String prenom,
            LocalDate dateNaissance,
            String adresse,
            String email,
            String telephone);

    void deleteClient(int id);

    List<Client> searchClients(String nom, String prenom, String email);

    List<Client> findAllClients();

    Client findClientById(int id);
}