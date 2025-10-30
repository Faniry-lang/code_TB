package com.management.courant.service;

import com.management.courant.entity.StatutCompte;
import com.management.courant.remote.StatutCompteRemote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class StatutCompteService implements StatutCompteRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<StatutCompte> findAll() {
        TypedQuery<StatutCompte> query = em.createQuery("SELECT s FROM StatutCompte s ORDER BY s.id", StatutCompte.class);
        return query.getResultList();
    }

    @Override
    public StatutCompte findById(int id) {
        return em.find(StatutCompte.class, id);
    }

    @Override
    public StatutCompte save(StatutCompte statutCompte) {
        em.persist(statutCompte);
        return statutCompte;
    }

    @Override
    public StatutCompte update(StatutCompte statutCompte) {
        return em.merge(statutCompte);
    }

    @Override
    public void delete(int id) {
        StatutCompte statutCompte = findById(id);
        if (statutCompte != null) {
            em.remove(statutCompte);
        }
    }
}

