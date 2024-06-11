package javatechy.codegen.service;

import javatechy.codegen.dto.User;

public interface UserService {
    User findUserById(Long id);

    User findByUsernameAndPassword(String username, String password);

    User createUser(User user);

    boolean authenticateUser(String username, String password);
}
