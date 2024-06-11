package javatechy.codegen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import javatechy.codegen.dto.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
}