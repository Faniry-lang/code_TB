package com.management.courant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "action_role", schema = "public")
public class ActionRole implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role", nullable = false)
    private Integer role;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}