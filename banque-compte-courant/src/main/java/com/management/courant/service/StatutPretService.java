package com.management.courant.service;

import com.management.courant.entity.StatutPret;
import com.management.courant.remote.StatutPretRemote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class StatutPretService implements StatutPretRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<StatutPret> findAll() {
        TypedQuery<StatutPret> query = em.createQuery("SELECT s FROM StatutPret s ORDER BY s.id", StatutPret.class);
        return query.getResultList();
    }

    @Override
    public StatutPret findById(int id) {
        return em.find(StatutPret.class, id);
    }

    @Override
    public StatutPret save(StatutPret statutPret) {
        em.persist(statutPret);
        return statutPret;
    }

    @Override
    public StatutPret update(StatutPret statutPret) {
        return em.merge(statutPret);
    }

    @Override
    public void delete(int id) {
        StatutPret statutPret = findById(id);
        if (statutPret != null) {
            em.remove(statutPret);
        }
    }
}

