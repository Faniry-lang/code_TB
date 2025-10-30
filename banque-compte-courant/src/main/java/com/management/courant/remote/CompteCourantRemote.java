package com.management.courant.remote;

import com.management.courant.entity.CompteCourant;
import com.management.courant.entity.MouvementCompteCourant;
import jakarta.ejb.Remote;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Remote
public interface CompteCourantRemote {
    List<CompteCourant> findAll();
    CompteCourant findById(int id);
    CompteCourant save(CompteCourant compteCourant);
    CompteCourant update(CompteCourant compteCourant);
    void delete(int id);

    CompteCourant findByIdClient(int idClient);

    // Calcule le solde brut du compte courant à l'instant T
    BigDecimal calculerSoldeBrut(int idClient);

    // Créditer le compte courant d'un client
    void crediterCompteCourant(
            int idClient,
            BigDecimal montant,
            Instant dateMouvement,
            String description);

    // Débite le compte courant d'un client
    void debiterCompteCourant(int idClient,
                              BigDecimal montant,
                              Instant dateMouvement,
                              String description);

    // Historique des mouvements du compte courant d'un client
    List<MouvementCompteCourant> historiqueMouvementCompteCourant(int idClient);
}
