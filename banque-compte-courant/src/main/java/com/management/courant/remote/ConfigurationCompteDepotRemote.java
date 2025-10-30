package com.management.courant.remote;

import com.management.courant.entity.ConfigurationCompteDepot;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface ConfigurationCompteDepotRemote {
    List<ConfigurationCompteDepot> findAll();
    ConfigurationCompteDepot findById(int id);
    ConfigurationCompteDepot save(ConfigurationCompteDepot configurationCompteDepot);
    ConfigurationCompteDepot update(ConfigurationCompteDepot configurationCompteDepot);
    void delete(int id);

    ConfigurationCompteDepot findLatestByCompteDepotId(int idCompteDepot);
}
