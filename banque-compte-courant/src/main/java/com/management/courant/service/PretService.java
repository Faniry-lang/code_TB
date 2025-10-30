package com.management.courant.service;

import com.management.courant.entity.Pret;
import com.management.courant.remote.PretRemote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

@Stateless
public class PretService implements PretRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<Pret> findAll() {
        TypedQuery<Pret> query = em.createQuery("SELECT p FROM Pret p ORDER BY p.id", Pret.class);
        return query.getResultList();
    }

    @Override
    public Pret findById(int id) {
        return em.find(Pret.class, id);
    }

    @Override
    public Pret save(Pret pret) {
        em.persist(pret);
        return pret;
    }

    @Override
    public Pret update(Pret pret) {
        return em.merge(pret);
    }

    @Override
    public void delete(int id) {
        Pret pret = findById(id);
        if (pret != null) {
            em.remove(pret);
        }
    }

    @Override
    public BigDecimal findSommePretsByClientId(int idClient) {
        TypedQuery<BigDecimal> query = em.createQuery(
                "SELECT SUM(p.montantPret) FROM Pret p WHERE p.idClient.id = :idClient AND p.dateFermeture IS NULL",
                BigDecimal.class);
        query.setParameter("idClient", idClient);
        try {
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<Pret> findPretsByClientId(int idClient) {
        TypedQuery<Pret> query = em.createQuery(
                "SELECT p FROM Pret p WHERE p.idClient.id = :idClient", Pret.class);
        query.setParameter("idClient", idClient);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Calcule la somme des prêts à l'instant T pour un client
     * @param idClient ID du client
     * @return Somme des montants des prêts non fermés
     */
    public BigDecimal calculerSommePrets(int idClient) {
        return findSommePretsByClientId(idClient);
    }
}

