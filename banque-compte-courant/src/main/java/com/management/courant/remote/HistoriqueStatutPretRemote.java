package com.management.courant.remote;

import com.management.courant.entity.HistoriqueStatutPret;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface HistoriqueStatutPretRemote {
    List<HistoriqueStatutPret> findAll();
    HistoriqueStatutPret findById(int id);
    HistoriqueStatutPret save(HistoriqueStatutPret historiqueStatutPret);
    HistoriqueStatutPret update(HistoriqueStatutPret historiqueStatutPret);
    void delete(int id);
}
