package com.management.courant.service;

import com.management.courant.entity.ActionRole;
import com.management.courant.entity.Direction;
import com.management.courant.entity.Utilisateur;
import com.management.courant.remote.ActionRoleRemote;
import com.management.courant.remote.DirectionRemote;
import com.management.courant.remote.UtilisateurRemote;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateful
public class UtilisateurService implements UtilisateurRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    private Utilisateur utilisateurConnecte;
    private List<Direction> toutesDirections;
    private List<ActionRole> toutesActionsRoles;
    private boolean sessionActive = false;

    @EJB
    private DirectionRemote directionRemote;

    @EJB
    private ActionRoleRemote actionRoleRemote;

    @Override
    public List<Utilisateur> findAll() {
        TypedQuery<Utilisateur> query = em.createQuery("SELECT u FROM Utilisateur u ORDER BY u.id", Utilisateur.class);
        return query.getResultList();
    }

    @Override
    public Utilisateur findById(int id) {
        return em.find(Utilisateur.class, id);
    }

    @Override
    public Utilisateur save(Utilisateur client) {
        em.persist(client);
        em.flush(); // Pour obtenir l'ID immédiatement
        return client;
    }

    @Override
    public Utilisateur update(Utilisateur client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        Utilisateur client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }

    @Override
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    @Override
    public List<Direction> getToutesDirections() {
        return toutesDirections;
    }

    @Override
    public List<ActionRole> getToutesActionsRoles() {
        return toutesActionsRoles;
    }

    @Override
    public boolean login(String matricule, String password) {
        List<Utilisateur> utilisateurs = findAll();

        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getMatricule().equals(matricule) &&
                    utilisateur.getPassword().equals(password)) {

                // Authentification réussie
                this.utilisateurConnecte = utilisateur;
                this.toutesDirections = directionRemote.findAll();
                this.toutesActionsRoles = actionRoleRemote.findAll();
                this.sessionActive = true;

                return true;
            }
        }
        return false;
    }

    /**
     * Méthode de déconnexion - réinitialise toutes les variables de session
     */
    @Remove
    public void logout() {
        this.utilisateurConnecte = null;
        this.toutesDirections = null;
        this.toutesActionsRoles = null;
        this.sessionActive = false;
        System.out.println("Session utilisateur fermée avec succès");
    }

    /**
     * Vérifie si une session est active
     */
    public boolean isSessionActive() {
        return sessionActive && utilisateurConnecte != null;
    }

    /**
     * Méthode appelée automatiquement avant la destruction du bean
     */
    @PreDestroy
    public void cleanup() {
        System.out.println("Nettoyage de la session UtilisateurService");
        logout();
    }
}