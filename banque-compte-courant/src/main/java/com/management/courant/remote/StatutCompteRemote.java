package com.management.courant.remote;

import com.management.courant.entity.StatutCompte;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface StatutCompteRemote {
    List<StatutCompte> findAll();
    StatutCompte findById(int id);
    StatutCompte save(StatutCompte statutCompte);
    StatutCompte update(StatutCompte statutCompte);
    void delete(int id);
}
