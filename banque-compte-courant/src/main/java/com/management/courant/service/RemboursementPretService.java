package com.management.courant.service;

import com.management.courant.entity.RemboursementPret;
import com.management.courant.remote.RemboursementPretRemote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class RemboursementPretService implements RemboursementPretRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<RemboursementPret> findAll() {
        TypedQuery<RemboursementPret> query = em.createQuery("SELECT r FROM RemboursementPret r ORDER BY r.id", RemboursementPret.class);
        return query.getResultList();
    }

    @Override
    public RemboursementPret findById(int id) {
        return em.find(RemboursementPret.class, id);
    }

    @Override
    public RemboursementPret save(RemboursementPret remboursementPret) {
        em.persist(remboursementPret);
        return remboursementPret;
    }

    @Override
    public RemboursementPret update(RemboursementPret remboursementPret) {
        return em.merge(remboursementPret);
    }

    @Override
    public void delete(int id) {
        RemboursementPret remboursementPret = findById(id);
        if (remboursementPret != null) {
            em.remove(remboursementPret);
        }
    }
}

