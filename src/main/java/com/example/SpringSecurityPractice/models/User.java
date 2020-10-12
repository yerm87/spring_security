package com.example.SpringSecurityPractice.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
//@NamedEntityGraph(name="Authority", attributeNodes = @NamedAttributeNode(value="authorities"))
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    private String password;

    @OneToOne
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_permission",
            joinColumns = { @JoinColumn(name="authorities") },
            inverseJoinColumns = { @JoinColumn(name="users") }
    )
    public Set<Authority> authorities = new HashSet<>();

    private String permissions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
