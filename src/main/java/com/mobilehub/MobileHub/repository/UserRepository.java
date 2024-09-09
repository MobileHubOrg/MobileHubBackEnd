package com.mobilehub.MobileHub.repository;

import com.mobilehub.MobileHub.model.User;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByUserId(@NonNull Long userId);

@Query("SELECT u FROM User u WHERE u.login = :login")
 Optional<User> findByLogin( String login);

    boolean existsByLogin(@NonNull String login);
    void deleteByLogin(@Param("login") String login);

}
