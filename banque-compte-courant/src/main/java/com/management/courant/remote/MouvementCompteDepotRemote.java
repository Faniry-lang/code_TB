package com.management.courant.remote;

import com.management.courant.entity.MouvementCompteDepot;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface MouvementCompteDepotRemote {
    List<MouvementCompteDepot> findAll();
    MouvementCompteDepot findById(int id);
    MouvementCompteDepot save(MouvementCompteDepot mouvementCompteDepot);
    MouvementCompteDepot update(MouvementCompteDepot mouvementCompteDepot);
    void delete(int id);

    List<MouvementCompteDepot> findByIdCompteDepot(int idCompteDepot);
    int countRetraitsThisMonth(int idCompteDepot);

    List<MouvementCompteDepot> findMouvementsHorsInterets(int idCompteDepot);
}
