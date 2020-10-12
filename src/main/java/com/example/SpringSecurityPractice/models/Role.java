package com.example.SpringSecurityPractice.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String userRole;

    @OneToOne
    @JoinColumn(name="role_id")
    private User user;

    @ManyToMany
    List<Authority> authorities = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
