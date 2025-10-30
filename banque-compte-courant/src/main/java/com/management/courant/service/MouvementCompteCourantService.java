package com.management.courant.service;

import com.management.courant.entity.MouvementCompteCourant;
import com.management.courant.remote.MouvementCompteCourantRemote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class MouvementCompteCourantService implements MouvementCompteCourantRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<MouvementCompteCourant> findAll() {
        TypedQuery<MouvementCompteCourant> query = em.createQuery("SELECT m FROM MouvementCompteCourant m ORDER BY m.id", MouvementCompteCourant.class);
        return query.getResultList();
    }

    @Override
    public MouvementCompteCourant findById(int id) {
        return em.find(MouvementCompteCourant.class, id);
    }

    @Override
    public MouvementCompteCourant save(MouvementCompteCourant mouvementCompteCourant) {
        em.persist(mouvementCompteCourant);
        return mouvementCompteCourant;
    }

    @Override
    public MouvementCompteCourant update(MouvementCompteCourant mouvementCompteCourant) {
        return em.merge(mouvementCompteCourant);
    }

    @Override
    public void delete(int id) {
        MouvementCompteCourant mouvementCompteCourant = findById(id);
        if (mouvementCompteCourant != null) {
            em.remove(mouvementCompteCourant);
        }
    }

    @Override
    public List<MouvementCompteCourant> findByIdCompteCourant(int idCompteCourant) {
        TypedQuery<MouvementCompteCourant> query = em.createQuery(
                "SELECT m FROM MouvementCompteCourant m WHERE m.idCompteCourant.id = :idCompteCourant ORDER BY m.dateMouvement DESC", MouvementCompteCourant.class);
        query.setParameter("idCompteCourant", idCompteCourant);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}

