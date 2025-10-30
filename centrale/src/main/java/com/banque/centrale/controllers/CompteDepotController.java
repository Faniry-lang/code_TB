package com.banque.centrale.controllers;

import com.management.courant.remote.ClientRemote;
import com.banque.devise.repository.DeviseRemote;
import com.banque.devise.entity.Devise;
import org.springframework.stereotype.Controller;
import com.management.courant.entity.Client;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/comptes-depot")
public class CompteDepotController {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:5186/api";

    private final ClientRemote clientRemote;
    private final DeviseRemote deviseRemote;

    public CompteDepotController(RestTemplate restTemplate, ClientRemote clientRemote, DeviseRemote deviseRemote) {
        this.restTemplate = restTemplate;
        this.clientRemote = clientRemote;
        this.deviseRemote = deviseRemote;
    }

    @GetMapping("/client/{idClient}")
    public String gestionCompteDepot(@PathVariable Integer idClient, Model model) {
        try {
            // Récupérer les informations du client
            Client clientObject = clientRemote.findById(idClient);
            Map<String, Object> client = new HashMap<>();
            client.put("id", clientObject.getId());
            client.put("matricule", clientObject.getMatricule());
            client.put("nom", clientObject.getNom());
            client.put("prenom", clientObject.getPrenom());
            client.put("dateNaissance", clientObject.getDateNaissance());
            client.put("adresse", clientObject.getAdresse());
            client.put("email", clientObject.getEmail());
            client.put("telephone", clientObject.getTelephone());
            client.put("dateCreation", clientObject.getDateCreation());

            model.addAttribute("client", client);

            // Solde brut
            Map<String, Object> soldeBrut = restTemplate.getForObject(
                    API_BASE_URL + "/comptes-depot/solde-brut/" + idClient,
                    Map.class
            );
            model.addAttribute("soldeBrut", soldeBrut);

            // Intérêts
            Map<String, Object> interets = restTemplate.getForObject(
                    API_BASE_URL + "/comptes-depot/interets/" + idClient,
                    Map.class
            );
            model.addAttribute("interets", interets);

            // Solde réel
            Map<String, Object> soldeReel = restTemplate.getForObject(
                    API_BASE_URL + "/comptes-depot/solde-reel/" + idClient,
                    Map.class
            );
            model.addAttribute("soldeReel", soldeReel);

            // Historique des mouvements
            Map<String, Object> historiqueResponse = restTemplate.getForObject(
                    API_BASE_URL + "/comptes-depot/historique/" + idClient,
                    Map.class
            );

            List<Map<String, Object>> historique = new ArrayList<>();
            if (historiqueResponse != null && historiqueResponse.containsKey("Mouvements")) {
                historique = (List<Map<String, Object>>) historiqueResponse.get("Mouvements");
            }
            model.addAttribute("historique", historique);

            // Récupérer les devises actives
            List<Devise> devisesActives = deviseRemote.obtenirDevisesActives();
            model.addAttribute("devises", devisesActives);

        } catch (Exception e) {
            model.addAttribute("error", "Compte dépôt non trouvé");
            model.addAttribute("historique", new ArrayList<>());
        }

        return "comptes/depot";
    }

    @PostMapping("/client/{idClient}/crediter")
    public String crediterCompteDepot(@PathVariable Integer idClient,
                                      @RequestParam Double montant,
                                      @RequestParam String description,
                                      @RequestParam String devise,
                                      RedirectAttributes redirectAttributes) {

        try {
            // Convertir le montant en ariary
            BigDecimal montantEnAriary = convertirMontantEnAriary(BigDecimal.valueOf(montant), devise);

            Map<String, Object> mouvementRequest = new HashMap<>();
            mouvementRequest.put("Montant", montantEnAriary.doubleValue());
            mouvementRequest.put("Description", description + " (Devise: " + devise + ")");
            mouvementRequest.put("DateMouvement", new Date().toInstant().toString());

            Map<String, Object> response = restTemplate.postForObject(
                    API_BASE_URL + "/comptes-depot/crediter/" + idClient,
                    mouvementRequest,
                    Map.class
            );

            if (response != null && Boolean.TRUE.equals(response.get("Success"))) {
                redirectAttributes.addFlashAttribute("success",
                        "Dépôt de " + montant + " " + devise + " effectué avec succès (" +
                                montantEnAriary.setScale(2, BigDecimal.ROUND_HALF_UP) + " Ariary)");
            } else {
                String errorMessage = (String) response.get("Error");
                redirectAttributes.addFlashAttribute("error", errorMessage != null ? errorMessage : "Erreur lors du dépôt");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération de dépôt: " + e.getMessage());
        }

        return "redirect:/comptes-depot/client/" + idClient;
    }

    @PostMapping("/client/{idClient}/debiter")
    public String debiterCompteDepot(@PathVariable Integer idClient,
                                     @RequestParam Double montant,
                                     @RequestParam String description,
                                     @RequestParam String devise,
                                     RedirectAttributes redirectAttributes) {

        try {
            // Convertir le montant en ariary
            BigDecimal montantEnAriary = convertirMontantEnAriary(BigDecimal.valueOf(montant), devise);

            Map<String, Object> mouvementRequest = new HashMap<>();
            mouvementRequest.put("Montant", montantEnAriary.doubleValue());
            mouvementRequest.put("Description", description + " (Devise: " + devise + ")");
            mouvementRequest.put("DateMouvement", new Date().toInstant().toString());

            Map<String, Object> response = restTemplate.postForObject(
                    API_BASE_URL + "/comptes-depot/debiter/" + idClient,
                    mouvementRequest,
                    Map.class
            );

            if (response != null && Boolean.TRUE.equals(response.get("Success"))) {
                redirectAttributes.addFlashAttribute("success",
                        "Retrait de " + montant + " " + devise + " effectué avec succès (" +
                                montantEnAriary.setScale(2, BigDecimal.ROUND_HALF_UP) + " Ariary)");
            } else {
                String errorMessage = (String) response.get("Error");
                redirectAttributes.addFlashAttribute("error", errorMessage != null ? errorMessage : "Erreur lors du retrait");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération de retrait: " + e.getMessage());
        }

        return "redirect:/comptes-depot/client/" + idClient;
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