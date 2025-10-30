package com.management.courant.remote;

import com.management.courant.entity.TypeMouvementCompteDepot;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface TypeMouvementCompteDepotRemote {
    List<TypeMouvementCompteDepot> findAll();
    TypeMouvementCompteDepot findById(int id);
    TypeMouvementCompteDepot save(TypeMouvementCompteDepot typeMouvementCompteDepot);
    TypeMouvementCompteDepot update(TypeMouvementCompteDepot typeMouvementCompteDepot);
    void delete(int id);
}
