package com.management.courant.remote;

import com.management.courant.entity.CompteDepot;
import com.management.courant.entity.MouvementCompteDepot;
import jakarta.ejb.Remote;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Remote
public interface CompteDepotRemote {
    List<CompteDepot> findAll();
    CompteDepot findById(int id);
    CompteDepot save(CompteDepot compteDepot);
    CompteDepot update(CompteDepot compteDepot);
    void delete(int id);

    CompteDepot findByIdClient(int idClient);

    // Calcule le solde brut du compte dépôt à l'instant T
    BigDecimal calculerSoldeBrut(int idClient);

    // Calcule les intérêts gagnés au sein du compte dépôt jusqu'à l'instant T avec prorata temporis
    BigDecimal calculerInteretsGagnes(int idClient);

    // Calcule le solde réel du compte dépôt (solde brut + intérêts) à l'instant T
    BigDecimal calculerSoldeReel(int idClient);

    // Crédite le compte dépôt d'un client
    void crediterCompteDepot(int idClient, BigDecimal montant, Instant dateMouvement, String description);

    // Débite le compte dépôt d'un client
    void debiterCompteDepot(int idClient, BigDecimal montant, Instant dateMouvement, String description);

    // Historique des mouvements du compte dépôt d'un client
    List<MouvementCompteDepot> historiqueMouvementCompteDepot(int idClient);

    CompteDepot getCompteDepotByClientId(int idClient);
}

