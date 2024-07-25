package javatechy.codegen.service;

import javatechy.codegen.dto.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findUserById(Long id);

    User findByUsernameAndPassword(String username, String password);

    User createUser(User user);


    User authenticateUser(String username, String password);

    void deleteUserById(Long id);

    List<User> getAllUsers();

    Optional<User> findByEmail(String email);

    void createPasswordResetToken(User user);

    Optional<User> findByResetToken(String token);

    void updatePassword(User user, String newPassword);
}
