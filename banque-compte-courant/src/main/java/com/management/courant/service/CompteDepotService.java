package com.management.courant.service;

import com.management.courant.entity.CompteDepot;
import com.management.courant.entity.ConfigurationCompteDepot;
import com.management.courant.entity.MouvementCompteDepot;
import com.management.courant.entity.TypeMouvementCompteDepot;
import com.management.courant.remote.CompteDepotRemote;
import com.management.courant.remote.ConfigurationCompteDepotRemote;
import com.management.courant.remote.MouvementCompteDepotRemote;
import com.management.courant.remote.TypeMouvementCompteDepotRemote;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Stateless
public class CompteDepotService implements CompteDepotRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    private static final int STATUT_COMPTE_ACTIF = 1;

    private static final int TYPE_MOUVEMENT_DEPOT = 1;
    private static final int TYPE_MOUVEMENT_RETRAIT = 2;

    @EJB
    MouvementCompteDepotRemote mouvementCompteDepotRemote;

    @EJB
    ConfigurationCompteDepotRemote configurationCompteDepotRemote;

    @EJB
    TypeMouvementCompteDepotRemote typeMouvementCompteDepotRemote;

    @Override
    public List<CompteDepot> findAll() {
        TypedQuery<CompteDepot> query = em.createQuery("SELECT c FROM CompteDepot c ORDER BY c.id", CompteDepot.class);
        return query.getResultList();
    }

    @Override
    public CompteDepot findById(int id) {
        return em.find(CompteDepot.class, id);
    }

    @Override
    public CompteDepot save(CompteDepot client) {
        em.persist(client);
        return client;
    }

    @Override
    public CompteDepot update(CompteDepot client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        CompteDepot client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }

    @Override
    public CompteDepot findByIdClient(int idClient) {
        TypedQuery<CompteDepot> query = em.createQuery(
                "SELECT c FROM CompteDepot c WHERE c.idClient.id = :idClient", CompteDepot.class);
        query.setParameter("idClient", idClient);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Calcule le solde brut du compte dépôt à l'instant T
     * @param idClient ID du client
     * @return Solde brut du compte dépôt
     */
    @Override
    public BigDecimal calculerSoldeBrut(int idClient) {
        CompteDepot compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Calculer le solde brut en sommant tous les mouvements (hors intérêts)
        List<MouvementCompteDepot> mouvements = mouvementCompteDepotRemote.findByIdCompteDepot(compte.getId());

        BigDecimal soldeBrut = BigDecimal.ZERO;
        for (MouvementCompteDepot mouvement : mouvements) {
            if (mouvement.getIdTypeMouvement().getId() == TYPE_MOUVEMENT_DEPOT) { // Dépôt
                soldeBrut = soldeBrut.add(mouvement.getMontant());
            } else if (mouvement.getIdTypeMouvement().getId() == TYPE_MOUVEMENT_RETRAIT) { // Retrait
                soldeBrut = soldeBrut.subtract(mouvement.getMontant());
            }
            // On ignore les intérêts (type 3) pour le solde brut
        }

        return soldeBrut;
    }

    /**
     * Calcule les intérêts pour une période donnée
     */
    private BigDecimal calculerInteretsPourPeriode(BigDecimal solde, double tauxAnnuel, long jours) {
        return solde.multiply(BigDecimal.valueOf(tauxAnnuel))
                .multiply(BigDecimal.valueOf(jours))
                .divide(BigDecimal.valueOf(36500), 10, RoundingMode.HALF_UP); // ÷ 365 et ÷ 100 pour le pourcentage
    }

    /**
     * Calcule les intérêts gagnés au sein du compte dépôt jusqu'à l'instant T avec prorata temporis
     * @param idClient ID du client
     * @return Montant des intérêts gagnés
     */
    @Override
    public BigDecimal calculerInteretsGagnes(int idClient) {
        CompteDepot compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Récupérer la dernière configuration
        ConfigurationCompteDepot derniereConfig = configurationCompteDepotRemote.findLatestByCompteDepotId(compte.getId());
        if (derniereConfig == null) {
            throw new IllegalStateException("Aucune configuration trouvée pour le compte dépôt ID: " + compte.getId());
        }

        // Récupérer tous les mouvements triés par date (hors intérêts)
        List<MouvementCompteDepot> mouvements = mouvementCompteDepotRemote.findMouvementsHorsInterets(compte.getId());

        // Ajouter un mouvement fictif pour la date de début (ouverture du compte)
        List<MouvementCompteDepot> tousLesMouvements = new ArrayList<>();
        MouvementCompteDepot mouvementOuverture = new MouvementCompteDepot();
        mouvementOuverture.setDateMouvement(compte.getDateOuverture());
        mouvementOuverture.setMontant(BigDecimal.ZERO);
        tousLesMouvements.add(mouvementOuverture);
        tousLesMouvements.addAll(mouvements);

        // Trier par date
        tousLesMouvements.sort(Comparator.comparing(MouvementCompteDepot::getDateMouvement));

        // Calculer les intérêts avec prorata temporis
        BigDecimal interetsTotaux = BigDecimal.ZERO;
        BigDecimal soldeCourant = BigDecimal.ZERO;

        for (int i = 0; i < tousLesMouvements.size() - 1; i++) {
            MouvementCompteDepot mouvementActuel = tousLesMouvements.get(i);
            MouvementCompteDepot mouvementSuivant = tousLesMouvements.get(i + 1);

            // Mettre à jour le solde courant
            if (mouvementActuel.getIdTypeMouvement() != null) {
                if (mouvementActuel.getIdTypeMouvement().getId() == TYPE_MOUVEMENT_DEPOT) { // Dépôt
                    soldeCourant = soldeCourant.add(mouvementActuel.getMontant());
                } else if (mouvementActuel.getIdTypeMouvement().getId() == TYPE_MOUVEMENT_RETRAIT) { // Retrait
                    soldeCourant = soldeCourant.subtract(mouvementActuel.getMontant());
                }
            }

            // Calculer la durée entre les mouvements en jours
            long jours = ChronoUnit.DAYS.between(
                    mouvementActuel.getDateMouvement().atZone(ZoneId.systemDefault()).toLocalDate(),
                    mouvementSuivant.getDateMouvement().atZone(ZoneId.systemDefault()).toLocalDate()
            );

            if (jours > 0 && soldeCourant.compareTo(BigDecimal.ZERO) > 0) {
                // Calculer les intérêts pour cette période
                BigDecimal interetsPeriodes = calculerInteretsPourPeriode(
                        soldeCourant, derniereConfig.getTauxInteretAnnuel(), jours);
                interetsTotaux = interetsTotaux.add(interetsPeriodes);
            }
        }

        // Calculer les intérêts pour la dernière période (dernier mouvement à maintenant)
        MouvementCompteDepot dernierMouvement = tousLesMouvements.get(tousLesMouvements.size() - 1);

        // Mettre à jour le solde courant pour le dernier mouvement
        if (dernierMouvement.getIdTypeMouvement() != null) {
            if (dernierMouvement.getIdTypeMouvement().getId() == 1) { // Dépôt
                soldeCourant = soldeCourant.add(dernierMouvement.getMontant());
            } else if (dernierMouvement.getIdTypeMouvement().getId() == 2) { // Retrait
                soldeCourant = soldeCourant.subtract(dernierMouvement.getMontant());
            }
        }

        long joursFin = ChronoUnit.DAYS.between(
                dernierMouvement.getDateMouvement().atZone(ZoneId.systemDefault()).toLocalDate(),
                Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
        );

        if (joursFin > 0 && soldeCourant.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal interetsFin = calculerInteretsPourPeriode(
                    soldeCourant, derniereConfig.getTauxInteretAnnuel(), joursFin);
            interetsTotaux = interetsTotaux.add(interetsFin);
        }

        return interetsTotaux.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcule le solde réel du compte dépôt (solde brut + intérêts) à l'instant T
     * @param idClient ID du client
     * @return Solde réel du compte dépôt
     */
    @Override
    public BigDecimal calculerSoldeReel(int idClient) {
        BigDecimal soldeBrut = calculerSoldeBrut(idClient);
        BigDecimal interets = calculerInteretsGagnes(idClient);

        return soldeBrut.add(interets);
    }

    /**
     * Crédite le compte dépôt d'un client
     * @param idClient ID du client
     * @param montant Montant à créditer
     * @param dateMouvement Date du mouvement
     * @param description Description du mouvement
     */
    @Override
    public void crediterCompteDepot(int idClient, BigDecimal montant, Instant dateMouvement, String description) {
        CompteDepot compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Validation du montant
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

//        if (utilisateurService.getUtilisateurConnecte().getRole()<2) {
//            throw new IllegalArgumentException("Role non satisfait de utilisateur");
//        }

        TypeMouvementCompteDepot typeMouvement = typeMouvementCompteDepotRemote.findById(TYPE_MOUVEMENT_DEPOT); // Dépôt

        // Créer le mouvement
        MouvementCompteDepot mouvement = new MouvementCompteDepot();
        mouvement.setMontant(montant);
        mouvement.setDescription(description);
        mouvement.setDateMouvement(dateMouvement);
        mouvement.setIdTypeMouvement(typeMouvement);
        mouvement.setIdCompteDepot(compte);

        mouvementCompteDepotRemote.save(mouvement);
    }

    /**
     * Débite le compte dépôt d'un client
     * @param idClient ID du client
     * @param montant Montant à débiter
     * @param dateMouvement Date du mouvement
     * @param description Description du mouvement
     */
    @Override
    public void debiterCompteDepot(int idClient, BigDecimal montant, Instant dateMouvement, String description) {
        CompteDepot compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Validation du montant
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        // Récupérer la dernière configuration
        ConfigurationCompteDepot config = configurationCompteDepotRemote.findLatestByCompteDepotId(compte.getId());
        if (config == null) {
            throw new IllegalStateException("Aucune configuration trouvée pour le compte dépôt");
        }

        // Vérifier le nombre maximal de retraits ce mois-ci
        int retraitsCeMois = mouvementCompteDepotRemote.countRetraitsThisMonth(compte.getId());
        if (retraitsCeMois >= config.getLimiteRetraitMensuel()) {
            throw new IllegalStateException("Limite de retraits mensuels atteinte: " + config.getLimiteRetraitMensuel());
        }

//        if (utilisateurService.getUtilisateurConnecte().getRole()<2) {
//            throw new IllegalArgumentException("Role non satisfait de utilisateur");
//        }

        // Vérifier le pourcentage maximum de retrait par rapport au solde réel
        BigDecimal soldeReel = calculerSoldeReel(idClient);
        BigDecimal pourcentageRetrait = montant.multiply(BigDecimal.valueOf(100))
                .divide(soldeReel, 2, RoundingMode.HALF_UP);

        if (pourcentageRetrait.compareTo(BigDecimal.valueOf(config.getPourcentageMaxRetrait())) > 0) {
            throw new IllegalStateException(String.format(
                    "Le retrait dépasse le pourcentage maximum autorisé: %.2f%% > %.2f%%",
                    pourcentageRetrait.doubleValue(), config.getPourcentageMaxRetrait()
            ));
        }

        // Vérifier que le solde brut est suffisant
        BigDecimal soldeBrut = calculerSoldeBrut(idClient);
        if (soldeBrut.compareTo(montant) < 0) {
            throw new IllegalStateException("Solde brut insuffisant pour effectuer le retrait");
        }

        TypeMouvementCompteDepot typeMouvement = typeMouvementCompteDepotRemote.findById(TYPE_MOUVEMENT_RETRAIT); // Retrait

        // Créer le mouvement
        MouvementCompteDepot mouvement = new MouvementCompteDepot();
        mouvement.setMontant(montant);
        mouvement.setDescription(description);
        mouvement.setDateMouvement(dateMouvement);
        mouvement.setIdTypeMouvement(typeMouvement);
        mouvement.setIdCompteDepot(compte);

        mouvementCompteDepotRemote.save(mouvement);
    }

    /**
     * (15) Historique des mouvements du compte dépôt d'un client
     * @param idClient ID du client
     */
    @Override
    public List<MouvementCompteDepot> historiqueMouvementCompteDepot(int idClient) {

        // Vérifier que le compte dépôt existe pour le client
        CompteDepot compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Obtenir les mouvements du compte dépôt
        return mouvementCompteDepotRemote.findByIdCompteDepot(compte.getId());
    }

    /**
     * Récupère le compte dépôt d'un client
     * @param idClient ID du client
     * @return Compte dépôt du client
     */
    @Override
    public CompteDepot getCompteDepotByClientId(int idClient) {
        CompteDepot compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte dépôt non trouvé pour le client ID: " + idClient);
        }
        return compte;
    }
}

