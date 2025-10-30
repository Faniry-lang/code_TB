package com.management.courant.remote;

import com.management.courant.entity.MouvementCompteCourant;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface MouvementCompteCourantRemote {
    List<MouvementCompteCourant> findAll();
    MouvementCompteCourant findById(int id);
    MouvementCompteCourant save(MouvementCompteCourant mouvementCompteCourant);
    MouvementCompteCourant update(MouvementCompteCourant mouvementCompteCourant);
    void delete(int id);

    List<MouvementCompteCourant> findByIdCompteCourant(int idCompteCourant);
}
