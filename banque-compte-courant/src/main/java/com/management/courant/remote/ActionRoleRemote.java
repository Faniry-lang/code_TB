package com.management.courant.remote;

import com.management.courant.entity.ActionRole;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface ActionRoleRemote {
    List<ActionRole> findAll();
    ActionRole findById(int id);
    ActionRole save(ActionRole actionRole);
    ActionRole update(ActionRole actionRole);
    void delete(int id);
}