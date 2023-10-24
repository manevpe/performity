package com.performity.myservice.users;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
  User findById(long id);

  User findByEmail(String email);

  @Transactional
  void deleteByEmail(String email);
}
