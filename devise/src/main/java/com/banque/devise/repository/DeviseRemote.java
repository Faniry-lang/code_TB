package com.banque.devise.repository;

import com.banque.devise.entity.Devise;
import java.util.List;
import jakarta.ejb.Remote;

@Remote
public interface DeviseRemote {

    /**
     * Récupère toutes les devises du fichier CSV
     */
    List<Devise> obtenirToutesDevises();

    /**
     * Récupère seulement les devises actives (dont la date courante est dans l'intervalle de validité)
     */
    List<Devise> obtenirDevisesActives();

    /**
     * Vérifie si une devise spécifique est active
     */
    boolean estDeviseActive(String libelle);
}