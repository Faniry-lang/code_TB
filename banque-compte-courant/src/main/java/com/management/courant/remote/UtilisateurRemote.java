package com.management.courant.remote;

import com.management.courant.entity.ActionRole;
import com.management.courant.entity.Direction;
import com.management.courant.entity.Utilisateur;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface UtilisateurRemote {
    List<Utilisateur> findAll();
    Utilisateur findById(int id);
    Utilisateur save(Utilisateur utilisateur);
    Utilisateur update(Utilisateur utilisateur);
    void delete(int id);

    Utilisateur getUtilisateurConnecte();
    List<Direction> getToutesDirections();
    List<ActionRole> getToutesActionsRoles();

    boolean login(String matricule, String password);
    // Méthode de déconnexion - réinitialise toutes les variables de session
    void logout();

    // Vérifie si une session est active
    boolean isSessionActive();

    // Méthode appelée automatiquement avant la destruction du bean
    void cleanup();
}