package com.banque.devise.service;

import com.banque.devise.entity.Devise;
import com.banque.devise.repository.DeviseRemote;
import com.banque.devise.util.CsvReader;
import java.util.List;
import jakarta.ejb.Stateless;
import java.time.LocalDate;

@Stateless
public class DeviseService implements DeviseRemote {

    @Override
    public List<Devise> obtenirToutesDevises() {
        try {
            return CsvReader.lireToutesDevises();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des devises", e);
        }
    }

    @Override
    public List<Devise> obtenirDevisesActives() {
        try {
            return CsvReader.lireDevisesActives();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des devises actives", e);
        }
    }

    @Override
    public boolean estDeviseActive(String libelle) {
        List<Devise> devisesActives = obtenirDevisesActives();
        return devisesActives.stream()
                .anyMatch(devise -> devise.getLibelle().equalsIgnoreCase(libelle));
    }
}
