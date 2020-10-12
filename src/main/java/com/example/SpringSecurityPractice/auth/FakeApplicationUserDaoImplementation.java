package com.example.SpringSecurityPractice.auth;

import com.example.SpringSecurityPractice.models.User;
import com.example.SpringSecurityPractice.repo.UserRepository;
import com.example.SpringSecurityPractice.security.ApplicationUserRole;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationUserDaoImplementation implements ApplicationUserDAO {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDaoImplementation(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApplicationUser selectApplicationUserByUsername(String username) {

        return getListOfApplicationUsers()
                .stream()
                .filter(applicationUser -> applicationUser.getUsername().equals(username))
                .findFirst().orElseThrow(() -> new IllegalStateException("No such user"));
    }

    public List<ApplicationUser> getListOfApplicationUsers(){
        List<ApplicationUser> listOfApplicationUsers = Lists.newArrayList(
                new ApplicationUser(
                        "tom-brennan",
                        passwordEncoder.encode("password"),
                        ApplicationUserRole.STUDENT.getGrantedAuthority(),
                        true,
                        true,
                        true,
                        true
                        ),
                new ApplicationUser(
                        "linda",
                        passwordEncoder.encode("password123"),
                        ApplicationUserRole.ADMIN.getGrantedAuthority(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "tom",
                        passwordEncoder.encode("password123"),
                        ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthority(),
                        true,
                        true,
                        true,
                        true
                )
        );
        return listOfApplicationUsers;
    }
}
