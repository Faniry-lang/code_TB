package com.banque.centrale.controllers;

import com.management.courant.entity.Client;
import com.management.courant.remote.ClientRemote;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Controller
@RequestMapping("/prets")
public class PretController {

    private final RestTemplate restTemplate;

    private final String API_BASE_URL = "http://localhost:5199/api";

    private final ClientRemote clientRemote;

    public PretController(RestTemplate restTemplate, ClientRemote clientRemote) {
        this.restTemplate = restTemplate;
        this.clientRemote = clientRemote;
    }

    @GetMapping("/client/{idClient}")
    public String gestionPrets(@PathVariable Integer idClient, Model model) {
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

            // Somme totale des prêts
            Map<String, Object> pretsResponse = restTemplate.getForObject(
                    API_BASE_URL + "/prets/client/" + idClient + "/somme",
                    Map.class
            );
            model.addAttribute("prets", pretsResponse);

        } catch (Exception e) {
            model.addAttribute("error", "Aucun prêt trouvé");
        }

        return "prets/gestion";
    }

    // Afficher le formulaire de création d'un prêt
    @GetMapping("/client/{idClient}/nouveau")
    public String afficherFormulaireCreation(@PathVariable Integer idClient, Model model) {
        try {
            // Récupérer les informations du client
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

        } catch (Exception e) {
            model.addAttribute("error", "Client non trouvé");
            return "redirect:/prets/client/" + idClient;
        }

        return "prets/nouveau-pret";
    }

    // Traiter la création d'un prêt - Version corrigée
    @PostMapping("/creer")
    public String creerPret(@RequestParam Integer idClient,
                            @RequestParam Double montantPret,
                            @RequestParam Double tauxInteretAnnuel,
                            @RequestParam Integer periodiciteRemboursement,
                            Model model) {

        try {
            // Préparer la requête pour l'API
            Map<String, Object> pretRequest = new HashMap<>();
            pretRequest.put("idClient", idClient);
            pretRequest.put("montantPret", montantPret);
            pretRequest.put("tauxInteretAnnuel", tauxInteretAnnuel);
            pretRequest.put("periodiciteRemboursement", periodiciteRemboursement);
            pretRequest.put("dateCreation", new Date().toInstant().toString());

            // Appeler l'API pour créer le prêt
            Map<String, Object> response = restTemplate.postForObject(
                    API_BASE_URL + "/prets",
                    pretRequest,
                    Map.class
            );

            // Vérifier si la création a réussi
            if (response != null && response.containsKey("idPret")) {
                model.addAttribute("success", "Prêt créé avec succès !");
            } else {
                model.addAttribute("error", "Erreur lors de la création du prêt");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création du prêt: " + e.getMessage());
        }

        return "redirect:/prets/client/" + idClient;
    }

}
