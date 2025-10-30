package com.management.courant.service;

import com.management.courant.entity.MouvementCompteDepot;
import com.management.courant.remote.MouvementCompteDepotRemote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Stateless
public class MouvementCompteDepotService implements MouvementCompteDepotRemote {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<MouvementCompteDepot> findAll() {
        TypedQuery<MouvementCompteDepot> query = em.createQuery("SELECT m FROM MouvementCompteDepot m ORDER BY m.id", MouvementCompteDepot.class);
        return query.getResultList();
    }

    @Override
    public MouvementCompteDepot findById(int id) {
        return em.find(MouvementCompteDepot.class, id);
    }

    @Override
    public MouvementCompteDepot save(MouvementCompteDepot client) {
        em.persist(client);
        return client;
    }

    @Override
    public MouvementCompteDepot update(MouvementCompteDepot client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        MouvementCompteDepot client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }

    @Override
    public List<MouvementCompteDepot> findByIdCompteDepot(int idCompteDepot) {
        TypedQuery<MouvementCompteDepot> query = em.createQuery(
                "SELECT m FROM MouvementCompteDepot m WHERE m.idCompteDepot.id = :idCompteDepot ORDER BY m.dateMouvement DESC",
                MouvementCompteDepot.class);
        query.setParameter("idCompteDepot", idCompteDepot);
        return query.getResultList();
    }

    @Override
    public int countRetraitsThisMonth(int idCompteDepot) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(m) FROM MouvementCompteDepot m WHERE m.idCompteDepot.id = :idCompteDepot " +
                        "AND m.idTypeMouvement.id = 2 " + // Retrait
                        "AND m.dateMouvement BETWEEN :startOfMonth AND :endOfMonth",
                Long.class);
        query.setParameter("idCompteDepot", idCompteDepot);
        query.setParameter("startOfMonth", startOfMonth.atZone(ZoneId.systemDefault()).toInstant());
        query.setParameter("endOfMonth", endOfMonth.atZone(ZoneId.systemDefault()).toInstant());

        return query.getSingleResult().intValue();
    }

    @Override
    public List<MouvementCompteDepot> findMouvementsHorsInterets(int idCompteDepot) {
        TypedQuery<MouvementCompteDepot> query = em.createQuery(
                "SELECT m FROM MouvementCompteDepot m WHERE m.idCompteDepot.id = :idCompteDepot " +
                        "AND m.idTypeMouvement.id IN (1, 2) " + // Dépôt et Retrait seulement (hors intérêts)
                        "ORDER BY m.dateMouvement",
                MouvementCompteDepot.class);
        query.setParameter("idCompteDepot", idCompteDepot);
        return query.getResultList();
    }
}

