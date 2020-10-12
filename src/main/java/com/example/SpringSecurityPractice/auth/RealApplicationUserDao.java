package com.example.SpringSecurityPractice.auth;

import com.example.SpringSecurityPractice.models.Authority;
import com.example.SpringSecurityPractice.models.Role;
import com.example.SpringSecurityPractice.models.User;
import com.example.SpringSecurityPractice.repo.AuthorityRepository;
import com.example.SpringSecurityPractice.repo.UserRepository;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("real")
public class RealApplicationUserDao implements ApplicationUserDAO {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public RealApplicationUserDao(UserRepository userRepository,
                                  AuthorityRepository authorityRepository) {

        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;

    }

    public ApplicationUser selectApplicationUserByUsername(String username){
       /* List<ApplicationUser> allUsers = fetchAllUsersAndConvertToUserDetails();
        Optional<ApplicationUser> myUser = allUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();*/

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("user not found"));

        Role role = user.getRole();

        Integer id = role.getId();

        List<Authority> authorities = authorityRepository.getAuthoritiesOfUser(id);
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities
                .stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getName()))
                .collect(Collectors.toList());

        Set<SimpleGrantedAuthority> grantedAuthoritiesSet = new HashSet<>();

        for(SimpleGrantedAuthority simpleGrantedAuthority : simpleGrantedAuthorities){
            grantedAuthoritiesSet.add(simpleGrantedAuthority);
        }

        grantedAuthoritiesSet.add(new SimpleGrantedAuthority((user.getRole().getUserRole())));

        ApplicationUser applicationUser = new ApplicationUser(
                user.getUsername(),
                user.getPassword(),
                grantedAuthoritiesSet,
                true,
                true,
                true,
                true
        );

        return applicationUser;
    }

    private List<ApplicationUser> fetchAllUsersAndConvertToUserDetails(){
        List<User> users = userRepository.findAll();

        List<ApplicationUser> allUsers = users.stream()
                .map(user -> {
                    List<String> permissions = Arrays.asList(user.getPermissions().split(", "));

                    Set<GrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
                    simpleGrantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getUserRole()));

                    for(String permission: permissions){
                        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(permission));
                    }

                    ApplicationUser applicationUser = new ApplicationUser (
                            user.getUsername(),
                            user.getPassword(),
                            simpleGrantedAuthorities,
                            true,
                            true,
                            true,
                            true
                    );

                    return applicationUser;
                }).collect(Collectors.toList());
        return allUsers;
    }
}

//Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
                    /*Set<GrantedAuthority> simpleGrantedAuthorities = user.getRole().getAuthorities()
                            .stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                            .collect(Collectors.toSet());
                    simpleGrantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getUserRole()));*/
//ApplicationUser applicationUser =
