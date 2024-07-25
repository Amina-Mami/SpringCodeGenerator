package javatechy.codegen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import javatechy.codegen.dto.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);


    User findByUsername(String username);

    Optional<User> findById(Long id);

    void deleteById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String token);
}