package pe.edu.upc.patitas.login.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.patitas.login.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  @Query("select u from User u where u.username =:username and u.password=:password")
  Optional<User> findByUsernameAndPassword(String username, String password);
  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

}
