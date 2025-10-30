package com.management.courant.remote;

import com.management.courant.entity.Pret;
import jakarta.ejb.Remote;

import java.math.BigDecimal;
import java.util.List;

@Remote
public interface PretRemote {
    List<Pret> findAll();
    Pret findById(int id);
    Pret save(Pret pret);
    Pret update(Pret pret);
    void delete(int id);

    BigDecimal findSommePretsByClientId(int idClient);
    List<Pret> findPretsByClientId(int idClient);

    BigDecimal calculerSommePrets(int idClient);
}
