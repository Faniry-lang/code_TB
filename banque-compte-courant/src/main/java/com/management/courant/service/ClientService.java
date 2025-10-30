package com.management.courant.service;

import com.management.courant.entity.*;
import com.management.courant.remote.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class ClientService implements ClientRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @EJB
    StatutCompteRemote statutCompteRemote;

    @EJB
    CompteCourantRemote compteCourantRemote;

    @EJB
    CompteDepotRemote compteDepotRemote;

    @EJB
    ConfigurationCompteDepotRemote configurationCompteDepotRemote;

    @EJB
    PretRemote pretRemote;

    @Override
    public List<Client> findAll() {
        TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c ORDER BY c.id", Client.class);
        return query.getResultList();
    }

    @Override
    public Client findById(int id) {
        return em.find(Client.class, id);
    }

    @Override
    public Client save(Client client) {
        em.persist(client);
        em.flush(); // Pour obtenir l'ID immédiatement
        return client;
    }

    @Override
    public Client update(Client client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        Client client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }

    @Override
    public List<Client> findByCriteria(String nom, String prenom, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> cq = cb.createQuery(Client.class);
        Root<Client> client = cq.from(Client.class);

        List<Predicate> predicates = new ArrayList<>();

        // Ajout des critères de recherche seulement s'ils ne sont pas null ou vides
        if (nom != null && !nom.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(client.get("nom")), "%" + nom.toLowerCase() + "%"));
        }

        if (prenom != null && !prenom.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(client.get("prenom")), "%" + prenom.toLowerCase() + "%"));
        }

        if (email != null && !email.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(client.get("email")), "%" + email.toLowerCase() + "%"));
        }

        // Si aucun critère n'est spécifié, retourner tous les clients
        if (predicates.isEmpty()) {
            return findAll();
        }

        // Application des prédicats et tri par ID
        cq.where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.asc(client.get("id")));

        TypedQuery<Client> query = em.createQuery(cq);
        return query.getResultList();
    }

    /**
     *  Crée un nouveau client avec ses comptes associés
     */
    @Override
    public Client creerClient(String nom, String prenom, LocalDate dateNaissance,
                              String adresse, String email, String telephone) {

        // Générer un matricule unique
        String matricule = "CLI" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Créer le client
        Client client = new Client();
        client.setMatricule(matricule);
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setDateNaissance(dateNaissance);
        client.setAdresse(adresse);
        client.setEmail(email);
        client.setTelephone(telephone);
        client.setDateCreation(Instant.now());

        client = save(client);

        // Récupérer le statut "Actif"
        StatutCompte statutActif = statutCompteRemote.findById(1);
        if (statutActif == null) {
            throw new IllegalStateException("Statut 'Actif' non trouvé dans la base de données");
        }

        // Créer le compte courant
        CompteCourant compteCourant = new CompteCourant();
        compteCourant.setNumeroCompte("CC" + client.getId());
        compteCourant.setSolde(BigDecimal.ZERO);
        compteCourant.setDateOuverture(Instant.now());
        compteCourant.setDateFermeture(null);
        compteCourant.setIdStatut(statutActif);
        compteCourant.setIdClient(client);

        compteCourantRemote.save(compteCourant);

        // Créer le compte dépôt
        CompteDepot compteDepot = new CompteDepot();
        compteDepot.setNumeroCompte("CD" + client.getId());
        compteDepot.setDateOuverture(Instant.now());
        compteDepot.setDateFermeture(null);
        compteDepot.setIdStatut(statutActif);
        compteDepot.setIdClient(client);

        compteDepotRemote.save(compteDepot);

        // Créer une configuration par défaut pour le compte dépôt
        ConfigurationCompteDepot config = new ConfigurationCompteDepot();
        config.setTauxInteretAnnuel(1.5); // 1.5% par défaut
        config.setLimiteRetraitMensuel(3); // 3 retraits par mois par défaut
        config.setPourcentageMaxRetrait(30.0); // 30% du solde par défaut
        config.setDateApplication(Instant.now());
        config.setIdCompteDepot(compteDepot);

        configurationCompteDepotRemote.save(config);

        return client;
    }

    /**
     * Calcule le solde avec intérêts du compte dépôt (alias pour calculerSoldeReel)
     */
    @Override
    public BigDecimal calculerSoldeAvecInteret(int idClient) {
        return compteDepotRemote.calculerSoldeReel(idClient);
    }

    /**
     * (8) Calcule le solde total simplifié du client
     * solde compte courant + solde avec intérêts compte dépôt - somme des prêts
     */
    @Override
    public BigDecimal calculerSoldeTotale(int idClient) {
        // Vérifier que le client existe
        Client client = findById(idClient);
        if (client == null) {
            throw new EntityNotFoundException("Client non trouvé avec l'ID: " + idClient);
        }

        // (1) Solde brut compte courant
        BigDecimal soldeCourant = compteCourantRemote.calculerSoldeBrut(idClient);

        // (4) Solde avec intérêts compte dépôt
        BigDecimal soldeDepotAvecInterets = calculerSoldeAvecInteret(idClient);

        // (5) Calculer la somme des prêts non remboursés
        BigDecimal sommePrets = calculerSommePrets(idClient);

        // Calcul du solde total
        return soldeCourant.add(soldeDepotAvecInterets).subtract(sommePrets);
    }

    /**
     * (5) Calcule la somme des prêts non remboursés pour un client
     */
    private BigDecimal calculerSommePrets(int idClient) {
        // Utilisation du PretService injecté
        return pretRemote.calculerSommePrets(idClient);
    }

    @Override
    public List<Client> findAllClients() {
        return findAll();
    }

    @Override
    public Client findClientById(int id) {
        return findById(id);
    }

    @Override
    public Client updateClient(int id, String nom, String prenom, LocalDate dateNaissance,
                               String adresse, String email, String telephone) {
        Client client = findById(id);
        if (client != null) {
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setDateNaissance(dateNaissance);
            client.setAdresse(adresse);
            client.setEmail(email);
            client.setTelephone(telephone);
            return update(client);
        }
        throw new EntityNotFoundException("Client non trouvé avec l'ID: " + id);
    }

    @Override
    public void deleteClient(int id) {
        Client client = findById(id);
        if (client != null) {
            delete(id);
        } else {
            throw new EntityNotFoundException("Client non trouvé avec l'ID: " + id);
        }
    }

    @Override
    public List<Client> searchClients(String nom, String prenom, String email) {
        // Implémentation de la recherche
        return findByCriteria(nom, prenom, email);
    }
}