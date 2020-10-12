package com.example.SpringSecurityPractice.repo;

import com.example.SpringSecurityPractice.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    @Query(value="SELECT * FROM authorities WHERE id IN (SELECT authorities_id FROM roles_authorities WHERE roles_id=?)",
    nativeQuery = true)
    List<Authority> getAuthoritiesOfUser(Integer id);

    @Query(value="SELECT * FROM authorities",
    nativeQuery = true)
    List<Authority> testAuthority();
}
