package org.example.stockifyims.repository.user;

import org.example.stockifyims.entity.UserVo;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserVo, Long> {
    Optional<UserVo> findByUsername(String username);
    Optional<UserVo> findByEmail(String email);
}
