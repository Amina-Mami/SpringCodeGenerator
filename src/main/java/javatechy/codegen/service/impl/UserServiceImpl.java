package javatechy.codegen.service.impl;

import javatechy.codegen.dto.User;
import javatechy.codegen.Repository.UserRepository;
import javatechy.codegen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public User findUserById(Long id) {
        return userRepository.findOne(id);  // Directly return the user if found
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }


    @Override
    public User createUser(User user) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);


        return userRepository.save(user);
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {

            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
