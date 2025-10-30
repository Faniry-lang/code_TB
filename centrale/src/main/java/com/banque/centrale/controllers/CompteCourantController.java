package com.banque.centrale.controllers;

import com.management.courant.entity.CompteCourant;
import com.management.courant.entity.MouvementCompteCourant;
import com.management.courant.entity.Client;
import com.management.courant.remote.CompteCourantRemote;
import com.management.courant.remote.ClientRemote;
import com.banque.devise.repository.DeviseRemote;
import com.banque.devise.entity.Devise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comptes-courants")
public class CompteCourantController {

    @Autowired
    private CompteCourantRemote compteCourantRemote;

    @Autowired
    private ClientRemote clientRemote;

    @Autowired
    private DeviseRemote deviseRemote;

    /**
     * Affiche la page de gestion du compte courant d'un client
     */
    @GetMapping("/client/{idClient}")
    public String gestionCompteCourant(@PathVariable Integer idClient, Model model) {
        try {
            // Récupérer les informations du client via EJB
            Client client = clientRemote.findById(idClient);
            if (client == null) {
                model.addAttribute("error", "Client non trouvé");
                model.addAttribute("historique", new ArrayList<>());
                return "comptes/courant";
            }

            // Convertir Client en Map pour Thymeleaf (compatibilité avec le template)
            Map<String, Object> clientMap = new HashMap<>();
            clientMap.put("id", client.getId());
            clientMap.put("nom", client.getNom());
            clientMap.put("prenom", client.getPrenom());
            model.addAttribute("client", clientMap);

            // Récupérer le solde brut via EJB
            BigDecimal soldeBrut = compteCourantRemote.calculerSoldeBrut(idClient);
            Map<String, Object> soldeMap = new HashMap<>();
            soldeMap.put("solde", soldeBrut);
            model.addAttribute("solde", soldeMap);

            // Récupérer l'historique des mouvements via EJB
            List<MouvementCompteCourant> historique = compteCourantRemote.historiqueMouvementCompteCourant(idClient);

            // Convertir les mouvements en Maps pour Thymeleaf
            List<Map<String, Object>> historiqueMap = new ArrayList<>();
            for (MouvementCompteCourant mouvement : historique) {
                Map<String, Object> mouvementMap = new HashMap<>();
                mouvementMap.put("dateMouvement", mouvement.getDateMouvement().toString());
                mouvementMap.put("description", mouvement.getDescription());
                mouvementMap.put("typeMouvement", mouvement.getIdTypeMouvement().getLibelle());
                mouvementMap.put("montant", mouvement.getMontant());
                historiqueMap.add(mouvementMap);
            }
            model.addAttribute("historique", historiqueMap);

            // Récupérer les devises actives
            List<Devise> devisesActives = deviseRemote.obtenirDevisesActives();
            model.addAttribute("devises", devisesActives);

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération des données: " + e.getMessage());
            model.addAttribute("historique", new ArrayList<>());
        }

        return "comptes/courant";
    }

    /**
     * Créditer le compte courant d'un client
     */
    @PostMapping("/client/{idClient}/crediter")
    public String crediterCompteCourant(@PathVariable Integer idClient,
                                        @RequestParam Double montant,
                                        @RequestParam String description,
                                        @RequestParam String devise,
                                        RedirectAttributes redirectAttributes) {
        try {
            // Validation du montant
            if (montant == null || montant <= 0) {
                redirectAttributes.addFlashAttribute("error", "Le montant doit être positif");
                return "redirect:/comptes-courants/client/" + idClient;
            }

            // Convertir le montant en ariary selon la devise sélectionnée
            BigDecimal montantEnAriary = convertirMontantEnAriary(BigDecimal.valueOf(montant), devise);

            // Appel EJB pour créditer
            compteCourantRemote.crediterCompteCourant(
                    idClient,
                    montantEnAriary,
                    Instant.now(),
                    description + " (Devise: " + devise + ")"
            );

            redirectAttributes.addFlashAttribute("success",
                    "Crédit de " + montant + " " + devise + " effectué avec succès (" +
                            montantEnAriary.setScale(2, BigDecimal.ROUND_HALF_UP) + " Ariary)");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur de validation: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération de crédit: " + e.getMessage());
        }

        return "redirect:/comptes-courants/client/" + idClient;
    }

    /**
     * Débiter le compte courant d'un client
     */
    @PostMapping("/client/{idClient}/debiter")
    public String debiterCompteCourant(@PathVariable Integer idClient,
                                       @RequestParam Double montant,
                                       @RequestParam String description,
                                       @RequestParam String devise,
                                       RedirectAttributes redirectAttributes) {
        try {
            // Validation du montant
            if (montant == null || montant <= 0) {
                redirectAttributes.addFlashAttribute("error", "Le montant doit être positif");
                return "redirect:/comptes-courants/client/" + idClient;
            }

            // Convertir le montant en ariary selon la devise sélectionnée
            BigDecimal montantEnAriary = convertirMontantEnAriary(BigDecimal.valueOf(montant), devise);

            // Appel EJB pour débiter
            compteCourantRemote.debiterCompteCourant(
                    idClient,
                    montantEnAriary,
                    Instant.now(),
                    description + " (Devise: " + devise + ")"
            );

            redirectAttributes.addFlashAttribute("success",
                    "Débit de " + montant + " " + devise + " effectué avec succès (" +
                            montantEnAriary.setScale(2, BigDecimal.ROUND_HALF_UP) + " Ariary)");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur de validation: " + e.getMessage());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération de débit: " + e.getMessage());
        }

        return "redirect:/comptes-courants/client/" + idClient;
    }

    /**
     * Convertit un montant d'une devise en ariary
     */
    private BigDecimal convertirMontantEnAriary(BigDecimal montant, String codeDevise) {
        try {
            // Si la devise est déjà l'ariary, pas de conversion
            if ("ARI".equalsIgnoreCase(codeDevise) || "Ariary".equalsIgnoreCase(codeDevise)) {
                return montant;
            }

            // Récupérer toutes les devises actives
            List<Devise> devisesActives = deviseRemote.obtenirDevisesActives();

            // Trouver la devise sélectionnée
            Devise deviseSelectionnee = devisesActives.stream()
                    .filter(d -> d.getLibelle().equalsIgnoreCase(codeDevise))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Devise non trouvée: " + codeDevise));

            // Convertir en ariary : montant * valeur de la devise
            return montant.multiply(deviseSelectionnee.getValeur());

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la conversion de devise: " + e.getMessage(), e);
        }
    }
}