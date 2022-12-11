package com.project.integration.dao.repos;

import com.project.integration.dao.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByLogin(String login);

  @Transactional
  @Modifying
  @Query(value="update users set users.password=?1, users.status='ACTIVE' where users.login=?2", nativeQuery = true)
  void changePassword(String password, String login);
}
