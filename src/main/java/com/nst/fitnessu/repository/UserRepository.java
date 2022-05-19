package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long Id);
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);




}
