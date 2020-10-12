package com.example.SpringSecurityPractice.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface ApplicationUserDAO {
   ApplicationUser selectApplicationUserByUsername(String username);
}
