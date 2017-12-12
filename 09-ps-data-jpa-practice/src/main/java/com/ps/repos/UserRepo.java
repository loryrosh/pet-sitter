package com.ps.repos;

import com.ps.ents.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by iuliana.cosmina on 2/23/16.
 */
public interface UserRepo extends JpaRepository<User, Long> {

    @Query("select u from User u where u.firstName like %?1%")
    List<User> findAllByFirstName(String firstName);

    User findOneByUsername(@Param("un") String username);

    User findUsernameById(Long id);

    @Query("select count(u.id) from User u")
    long countUsers();
}
