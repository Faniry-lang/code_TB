package com.banque.centrale.controllers;

import com.management.courant.remote.ClientRemote;
import com.management.courant.entity.Client;
import jakarta.ejb.EJB;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientRemote clientRemote;

    public ClientController(ClientRemote clientRemote) {
        this.clientRemote = clientRemote;
    }

    // Liste des clients avec recherche
    @GetMapping
    public String listeClients(Model model,
                               @RequestParam(required = false) String nom,
                               @RequestParam(required = false) String prenom,
                               @RequestParam(required = false) String email) {
        try {
            List<Client> clients;

            if (nom != null || prenom != null || email != null) {
                // Recherche par critères
                clients = clientRemote.searchClients(nom, prenom, email);
            } else {
                // Tous les clients
                clients = clientRemote.findAllClients();
            }

            model.addAttribute("clients", clients);
            model.addAttribute("count", clients.size());

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des clients: " + e.getMessage());
        }

        return "clients/liste";
    }

    // Détail d'un client
    @GetMapping("/{id}")
    public String detailClient(@PathVariable Integer id, Model model) {
        try {
            // Informations du client
            Client client = clientRemote.findClientById(id);
            if (client == null) {
                model.addAttribute("error", "Client non trouvé");
                return "redirect:/clients";
            }
            model.addAttribute("client", client);

            // Solde avec intérêt
            BigDecimal soldeAvecInteret = clientRemote.calculerSoldeAvecInteret(id);
            model.addAttribute("soldeAvecInteret", soldeAvecInteret);

            // Solde total
            BigDecimal soldeTotal = clientRemote.calculerSoldeTotale(id);
            model.addAttribute("soldeTotal", soldeTotal);

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement du client: " + e.getMessage());
            return "redirect:/clients";
        }

        return "clients/detail";
    }

    // Formulaire de création
    @GetMapping("/nouveau")
    public String formulaireCreation(Model model) {
        model.addAttribute("client", new Client());
        return "clients/formulaire";
    }

    // Création d'un client
    @PostMapping
    public String creerClient(@RequestParam String nom,
                              @RequestParam String prenom,
                              @RequestParam LocalDate dateNaissance,
                              @RequestParam String adresse,
                              @RequestParam String email,
                              @RequestParam String telephone,
                              RedirectAttributes redirectAttributes) {
        try {
            Client client = clientRemote.creerClient(nom, prenom, dateNaissance, adresse, email, telephone);
            redirectAttributes.addFlashAttribute("success", "Client créé avec succès");
            return "redirect:/clients/" + client.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création du client: " + e.getMessage());
            return "redirect:/clients/nouveau";
        }
    }

    // Formulaire de modification
    @GetMapping("/{id}/modifier")
    public String formulaireModification(@PathVariable Integer id, Model model) {
        try {
            Client client = clientRemote.findClientById(id);
            if (client == null) {
                return "redirect:/clients";
            }
            model.addAttribute("client", client);
        } catch (Exception e) {
            return "redirect:/clients";
        }
        return "clients/formulaire";
    }

    // Modification d'un client
    @PostMapping("/{id}")
    public String modifierClient(@PathVariable Integer id,
                                 @RequestParam String nom,
                                 @RequestParam String prenom,
                                 @RequestParam LocalDate dateNaissance,
                                 @RequestParam String adresse,
                                 @RequestParam String email,
                                 @RequestParam String telephone,
                                 RedirectAttributes redirectAttributes) {
        try {
            Client client = clientRemote.updateClient(id, nom, prenom, dateNaissance, adresse, email, telephone);
            redirectAttributes.addFlashAttribute("success", "Client modifié avec succès");
            return "redirect:/clients/" + client.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification du client: " + e.getMessage());
            return "redirect:/clients/" + id + "/modifier";
        }
    }

    // Suppression d'un client
    @PostMapping("/{id}/supprimer")
    public String supprimerClient(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clientRemote.deleteClient(id);
            redirectAttributes.addFlashAttribute("success", "Client supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du client: " + e.getMessage());
        }
        return "redirect:/clients";
    }
}