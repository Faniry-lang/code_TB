package com.management.courant.remote;

import com.management.courant.entity.TypeMouvementCompteCourant;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface TypeMouvementCompteCourantRemote {
    List<TypeMouvementCompteCourant> findAll();
    TypeMouvementCompteCourant findById(int id);
    TypeMouvementCompteCourant save(TypeMouvementCompteCourant typeMouvementCompteCourant);
    TypeMouvementCompteCourant update(TypeMouvementCompteCourant typeMouvementCompteCourant);
    void delete(int id);
}
