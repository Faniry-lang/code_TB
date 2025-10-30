package com.banque.centrale.controllers;

import com.management.courant.entity.Client;
import com.management.courant.remote.ClientRemote;
import com.management.courant.remote.CompteCourantRemote;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final RestTemplate restTemplate;

    private final String API_BASE_URL_DEPOT = "http://localhost:5186/api";
    private final String API_BASE_URL_PRET = "http://localhost:5199/api";

    private final ClientRemote clientRemote;

    private final CompteCourantRemote compteRemote;

    public DashboardController(RestTemplate restTemplate, ClientRemote clientRemote, CompteCourantRemote compteRemote) {
        this.restTemplate = restTemplate;
        this.clientRemote = clientRemote;
        this.compteRemote = compteRemote;
    }

    @GetMapping
    public String dashboard(Model model) {
        try {
            // Récupérer tous les clients
            List<Client> listClients = clientRemote.findAll();

            if (listClients != null) {
                // Nombre total de clients
                Integer clientCount = listClients.size();
                model.addAttribute("clientCount", clientCount);

                // Liste des clients récents
                model.addAttribute("recentClients", listClients);

                // Calcul des totaux
                double totalSolde = 0;
                double totalComptesCourants = 0;
                double totalComptesDepot = 0;
                double totalInterets = 0;
                double totalPrets = 0;

                for (Client client : listClients) {
                    Integer clientId = client.getId();

                    // Solde total du client
                    double soldeResponse = clientRemote.calculerSoldeTotale(clientId).doubleValue();
                    if (soldeResponse != 0) {
                        totalSolde += soldeResponse;
                    }

                    // Solde compte courant
                    BigDecimal ccResponse = compteRemote.calculerSoldeBrut(clientId);

                    if (ccResponse != null) {
                        totalComptesCourants += ccResponse.doubleValue();
                    }

                    // Solde compte dépôt
                    Map<String, Object> cdResponse = restTemplate.getForObject(
                            API_BASE_URL_DEPOT + "/comptes-depot/solde-brut/" + clientId,
                            Map.class
                    );
                    if (cdResponse != null) {
                        if (cdResponse.get("Solde") instanceof Integer) {
                            totalComptesDepot += (Integer) cdResponse.get("Solde");
                        } else if (cdResponse.get("Solde") instanceof Double) {
                            totalComptesDepot += (Double) cdResponse.get("Solde");
                        }
                    }

                    // Intérêts compte dépôt
                    Map<String, Object> interetsResponse = restTemplate.getForObject(
                            API_BASE_URL_DEPOT + "/comptes-depot/interets/" + clientId,
                            Map.class
                    );

                    if (interetsResponse != null && !interetsResponse.isEmpty()) {
                        totalInterets += (Double) interetsResponse.get("Solde");
                    }

                    // Prêts
                    Map<String, Object> pretsResponse = restTemplate.getForObject(
                            API_BASE_URL_PRET + "/prets/client/" + clientId + "/somme",
                            Map.class
                    );
                    if (pretsResponse != null) {
                        if (pretsResponse.get("solde") instanceof Integer) {
                            totalPrets += (Integer) pretsResponse.get("solde");
                        } else if (pretsResponse.get("solde") instanceof Double) {
                            totalPrets += (Double) pretsResponse.get("solde");
                        }
                    }
                }

                model.addAttribute("totalSolde", totalSolde);
                model.addAttribute("totalComptesCourants", totalComptesCourants);
                model.addAttribute("totalComptesDepot", totalComptesDepot);
                model.addAttribute("totalInterets", totalInterets);
                model.addAttribute("totalPrets", totalPrets);
            }

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "dashboard";
    }
}
