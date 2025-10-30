package com.management.courant.remote;

import com.management.courant.entity.Direction;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface DirectionRemote {
    List<Direction> findAll();
    Direction findById(int id);
    Direction save(Direction direction);
    Direction update(Direction direction);
    void delete(int id);
}