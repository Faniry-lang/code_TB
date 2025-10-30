package com.management.courant.service;

import com.management.courant.entity.ActionRole;
import com.management.courant.remote.ActionRoleRemote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ActionRoleService implements ActionRoleRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<ActionRole> findAll() {
        TypedQuery<ActionRole> query = em.createQuery("SELECT a FROM ActionRole a ORDER BY a.id", ActionRole.class);
        return query.getResultList();
    }

    @Override
    public ActionRole findById(int id) {
        return em.find(ActionRole.class, id);
    }

    @Override
    public ActionRole save(ActionRole client) {
        em.persist(client);
        em.flush(); // Pour obtenir l'ID immédiatement
        return client;
    }

    @Override
    public ActionRole update(ActionRole client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        ActionRole client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }
}