package com.banque.centrale.services.ejb;

import com.banque.centrale.config.EJBConfig;
import com.management.courant.remote.UtilisateurRemote;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;

@Component
public class UtilisateurSessionManager {

    private final EJBConfig ejbConfig;

    public UtilisateurSessionManager(EJBConfig ejbConfig) {
        this.ejbConfig = ejbConfig;
    }

    public UtilisateurRemote getUtilisateurSession(HttpSession session) throws NamingException {
        try {
            UtilisateurRemote utilisateurRemote = (UtilisateurRemote) session.getAttribute("utilisateurRemote");

            // Vérifier si l'EJB existe encore
            if (utilisateurRemote != null) {
                try {
                    // Test simple pour vérifier que l'EJB est toujours valide
                    utilisateurRemote.isSessionActive();
                    return utilisateurRemote;
                } catch (Exception e) {
                    // L'EJB est invalide, on le recrée
                    session.removeAttribute("utilisateurRemote");
                }
            }

            // Créer une nouvelle instance
            utilisateurRemote = ejbConfig.createUtilisateurRemote();
            session.setAttribute("utilisateurRemote", utilisateurRemote);
            return utilisateurRemote;

        } catch (NamingException e) {
            throw new NamingException("Erreur lors de la création de la session utilisateur: " + e.getMessage());
        }
    }

    public void destroyUtilisateurSession(HttpSession session) {
        try {
            UtilisateurRemote utilisateurRemote = (UtilisateurRemote) session.getAttribute("utilisateurRemote");
            if (utilisateurRemote != null) {
                try {
                    utilisateurRemote.logout();
                } catch (Exception e) {
                    // Ignorer si l'EJB est déjà expiré
                    System.err.println("EJB déjà expiré lors du logout: " + e.getMessage());
                }
                session.removeAttribute("utilisateurRemote");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la destruction de la session: " + e.getMessage());
        }
    }
}