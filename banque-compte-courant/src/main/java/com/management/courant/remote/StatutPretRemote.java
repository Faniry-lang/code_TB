package com.management.courant.remote;

import com.management.courant.entity.StatutPret;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface StatutPretRemote {
    List<StatutPret> findAll();
    StatutPret findById(int id);
    StatutPret save(StatutPret statutPret);
    StatutPret update(StatutPret statutPret);
    void delete(int id);
}
