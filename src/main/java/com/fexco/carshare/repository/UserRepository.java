package com.fexco.carshare.repository;

import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fexco.carshare.domain.User;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);
    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(DateTime dateTime);
    Optional<User> findOneByEmail(String email);
    Optional<User> findOneByLogin(String login);
    User findByLogin(String login);
    void delete(User t);
}
