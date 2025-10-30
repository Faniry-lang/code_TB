package com.management.courant.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "utilisateur", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "utilisateur_matricule_key", columnNames = {"matricule"})
})
public class Utilisateur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "matricule", nullable = false, length = 50)
    private String matricule;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private Integer role;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_direction", nullable = false)
    private Direction idDirection;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Direction getIdDirection() {
        return idDirection;
    }

    public void setIdDirection(Direction idDirection) {
        this.idDirection = idDirection;
    }

}