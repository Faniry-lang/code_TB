package com.management.courant.service;

import com.management.courant.entity.Direction;
import com.management.courant.remote.DirectionRemote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class DirectionService implements DirectionRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<Direction> findAll() {
        TypedQuery<Direction> query = em.createQuery("SELECT d FROM Direction d ORDER BY d.id", Direction.class);
        return query.getResultList();
    }

    @Override
    public Direction findById(int id) {
        return em.find(Direction.class, id);
    }

    @Override
    public Direction save(Direction client) {
        em.persist(client);
        em.flush(); // Pour obtenir l'ID immédiatement
        return client;
    }

    @Override
    public Direction update(Direction client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        Direction client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }
}