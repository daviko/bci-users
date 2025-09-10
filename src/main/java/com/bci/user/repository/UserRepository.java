package com.bci.user.repository;

import com.bci.user.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * Retrieves a user entity by its email address.
   * <p>
   * This query method leverages Spring Data JPA's method name conventions to generate
   * the corresponding query automatically.
   * </p>
   *
   * @param email the unique email address of the user
   * @return an {@link Optional} containing the {@link User} if found, or empty if no user exists with the given email
   */
  Optional<User> findByEmail(String email);
}
