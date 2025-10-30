package com.management.courant.remote;

import com.management.courant.entity.RemboursementPret;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface RemboursementPretRemote {
    List<RemboursementPret> findAll();
    RemboursementPret findById(int id);
    RemboursementPret save(RemboursementPret remboursementPret);
    RemboursementPret update(RemboursementPret remboursementPret);
    void delete(int id);
}
