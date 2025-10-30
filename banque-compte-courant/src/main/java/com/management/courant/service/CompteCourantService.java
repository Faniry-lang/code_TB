package com.management.courant.service;

import com.management.courant.entity.CompteCourant;
import com.management.courant.entity.MouvementCompteCourant;
import com.management.courant.entity.TypeMouvementCompteCourant;
import com.management.courant.remote.CompteCourantRemote;
import com.management.courant.remote.MouvementCompteCourantRemote;
import com.management.courant.remote.TypeMouvementCompteCourantRemote;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Stateless
public class CompteCourantService implements CompteCourantRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    private static final int STATUT_COMPTE_ACTIF = 1;

    private static final int TYPE_MOUVEMENT_CREDIT = 1;
    private static final int TYPE_MOUVEMENT_DEBIT = 2;

    @EJB
    private MouvementCompteCourantRemote mouvementCompteCourantRemote;

    @EJB
    private TypeMouvementCompteCourantRemote typeMouvementCompteCourantRemote;

    @Override
    public List<CompteCourant> findAll() {
        TypedQuery<CompteCourant> query = em.createQuery("SELECT c FROM CompteCourant c ORDER BY c.id", CompteCourant.class);
        return query.getResultList();
    }

    @Override
    public CompteCourant findById(int id) {
        return em.find(CompteCourant.class, id);
    }

    @Override
    public CompteCourant save(CompteCourant compteCourant) {
        em.persist(compteCourant);
        return compteCourant;
    }

    @Override
    public CompteCourant update(CompteCourant compteCourant) {
        return em.merge(compteCourant);
    }

    @Override
    public void delete(int id) {
        CompteCourant compteCourant = findById(id);
        if (compteCourant != null) {
            em.remove(compteCourant);
        }
    }


    // Dans CompteCourantRepositoryImp.java
    @Override
    public CompteCourant findByIdClient(int idClient) {
        TypedQuery<CompteCourant> query = em.createQuery(
                "SELECT c FROM CompteCourant c WHERE c.idClient.id = :idClient", CompteCourant.class);
        query.setParameter("idClient", idClient);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Calcule le solde brut du compte courant à l'instant T
     * @param idClient ID du client
     * @return Solde brut du compte courant
     */
    @Override
    public BigDecimal calculerSoldeBrut(int idClient) {
        CompteCourant compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte courant non trouvé pour le client ID: " + idClient);
        }
        return compte.getSolde();
    }

    /**
     * (6) Crédite le compte courant d'un client
     * @param idClient ID du client
     * @param montant Montant à créditer
     * @param dateMouvement Date du mouvement
     * @param description Description du mouvement
     */
    @Override
    public void crediterCompteCourant(int idClient, BigDecimal montant, Instant dateMouvement,
                                      String description) {
        CompteCourant compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte courant non trouvé pour le client ID: " + idClient);
        }

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

//        if (utilisateurService.getUtilisateurConnecte().getRole()<2) {
//            throw new IllegalArgumentException("Role non satisfait de utilisateur");
//        }

        TypeMouvementCompteCourant typeMouvement = typeMouvementCompteCourantRemote.findById(TYPE_MOUVEMENT_CREDIT); // Crédit

        // Mettre à jour le solde
        BigDecimal nouveauSolde = compte.getSolde().add(montant);
        compte.setSolde(nouveauSolde);
        update(compte);

        // Créer le mouvement
        MouvementCompteCourant mouvement = new MouvementCompteCourant();
        mouvement.setMontant(montant);
        mouvement.setDescription(description);
        mouvement.setDateMouvement(dateMouvement);
        mouvement.setIdTypeMouvement(typeMouvement);
        mouvement.setIdCompteCourant(compte);

        mouvementCompteCourantRemote.save(mouvement);
    }

    /**
     * (9) Débite le compte courant d'un client
     * @param idClient ID du client
     * @param montant Montant à débiter
     * @param dateMouvement Date du mouvement
     * @param description Description du mouvement
     */
    @Override
    public void debiterCompteCourant(int idClient, BigDecimal montant, Instant dateMouvement,
                                     String description) {
        // Validation du montant
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        CompteCourant compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte courant non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Vérifier le solde suffisant
        if (compte.getSolde().compareTo(montant) < 0) {
            throw new IllegalStateException("Solde insuffisant pour effectuer le débit");
        }

//        if (utilisateurService.getUtilisateurConnecte().getRole()<2) {
//            throw new IllegalArgumentException("Role non satisfait de utilisateur");
//        }

        TypeMouvementCompteCourant typeMouvement = typeMouvementCompteCourantRemote.findById(TYPE_MOUVEMENT_DEBIT); // Débit

        // Mettre à jour le solde
        BigDecimal nouveauSolde = compte.getSolde().subtract(montant);
        compte.setSolde(nouveauSolde);
        update(compte);

        // Créer le mouvement
        MouvementCompteCourant mouvement = new MouvementCompteCourant();
        mouvement.setMontant(montant);
        mouvement.setDescription(description);
        mouvement.setDateMouvement(dateMouvement);
        mouvement.setIdTypeMouvement(typeMouvement);
        mouvement.setIdCompteCourant(compte);

        mouvementCompteCourantRemote.save(mouvement);
    }

    /**
     * (14) Historique des mouvements du compte courant d'un client
     * @param idClient ID du client
     */
    @Override
    public List<MouvementCompteCourant> historiqueMouvementCompteCourant(int idClient) {

        // Vérifier que le compte courant existe pour le client
        CompteCourant compte = findByIdClient(idClient);
        if (compte == null) {
            throw new EntityNotFoundException("Compte courant non trouvé pour le client ID: " + idClient);
        }

        // Vérifier si le compte est actif
        if (compte.getIdStatut().getId().compareTo(STATUT_COMPTE_ACTIF) != 0) {
            throw new IllegalStateException("Impossible d'effectuer l'opération : le compte n'est pas actif");
        }

        // Obtenir les mouvements du compte courant
        return mouvementCompteCourantRemote.findByIdCompteCourant(compte.getId());
    }
}

