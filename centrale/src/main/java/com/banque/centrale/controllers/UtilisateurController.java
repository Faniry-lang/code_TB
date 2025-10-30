package com.banque.centrale.controllers;

import com.banque.centrale.services.ejb.UtilisateurSessionManager;
import com.management.courant.entity.Utilisateur;
import com.management.courant.remote.UtilisateurRemote;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.NamingException;

@Controller
@RequestMapping("/auth")
public class UtilisateurController {

    private final UtilisateurSessionManager utilisateurSessionManager;

    public UtilisateurController(UtilisateurSessionManager utilisateurSessionManager) {
        this.utilisateurSessionManager = utilisateurSessionManager;
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model) {
        try {
            UtilisateurRemote utilisateurRemote = utilisateurSessionManager.getUtilisateurSession(session);
            if (utilisateurRemote.isSessionActive()) {
                return "redirect:/dashboard";
            }
        } catch (Exception e) {
            // En cas d'erreur, on considère que la session n'est pas active
            model.addAttribute("error", "Session expirée, veuillez vous reconnecter");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String matricule,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            UtilisateurRemote utilisateurRemote = utilisateurSessionManager.getUtilisateurSession(session);

            boolean loginSuccess = utilisateurRemote.login(matricule, password);

            if (loginSuccess) {
                Utilisateur utilisateur = utilisateurRemote.getUtilisateurConnecte();

                // Stocker également l'utilisateur dans la session HTTP pour un accès facile
                session.setAttribute("utilisateur", utilisateur);
                session.setAttribute("matricule", matricule);

                redirectAttributes.addFlashAttribute("success", "Connexion réussie !");
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Matricule ou mot de passe incorrect");
                return "redirect:/auth/login";
            }

        } catch (NamingException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur de connexion au serveur: " + e.getMessage());
            return "redirect:/auth/login";
        } catch (Exception e) {
            // Gestion spécifique des EJB expirés
            if (e.getCause() instanceof jakarta.ejb.NoSuchEJBException) {
                try {
                    // Forcer la recréation de la session
                    session.removeAttribute("utilisateurRemote");
                    UtilisateurRemote newRemote = utilisateurSessionManager.getUtilisateurSession(session);
                    boolean loginSuccess = newRemote.login(matricule, password);

                    if (loginSuccess) {
                        Utilisateur utilisateur = newRemote.getUtilisateurConnecte();
                        session.setAttribute("utilisateur", utilisateur);
                        redirectAttributes.addFlashAttribute("success", "Connexion réussie !");
                        return "redirect:/dashboard";
                    }
                } catch (Exception retryException) {
                    redirectAttributes.addFlashAttribute("error", "Erreur de session, veuillez réessayer");
                }
            }
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la connexion: " + e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // Détruire la session EJB
            utilisateurSessionManager.destroyUtilisateurSession(session);

            // Nettoyer la session HTTP
            session.removeAttribute("utilisateur");
            session.removeAttribute("matricule");

            // Invalider la session HTTP
            session.invalidate();

            redirectAttributes.addFlashAttribute("success", "Déconnexion réussie");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la déconnexion: " + e.getMessage());
        }
        return "redirect:/auth/login";
    }

    // Méthode utilitaire pour vérifier si l'utilisateur est connecté
    public static boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("utilisateur") != null;
    }

    // Méthode pour récupérer l'utilisateur connecté depuis la session
    public static Utilisateur getLoggedInUser(HttpSession session) {
        return (Utilisateur) session.getAttribute("utilisateur");
    }

    // Méthode pour récupérer le matricule depuis la session
    public static String getLoggedInMatricule(HttpSession session) {
        return (String) session.getAttribute("matricule");
    }
}