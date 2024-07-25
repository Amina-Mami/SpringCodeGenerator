package javatechy.codegen.controller;

import io.swagger.annotations.Api;
import javatechy.codegen.Repository.AdminStatsRepository;
import javatechy.codegen.dto.AdminStats;
import javatechy.codegen.dto.ProjectCountPerDay;
import javatechy.codegen.dto.User;
import javatechy.codegen.service.AdminStatsService;
import javatechy.codegen.service.JsonFileService;
import javatechy.codegen.service.ProjectService;
import javatechy.codegen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(value = "User Controller")
@CrossOrigin(origins = "*")
@RequestMapping("user")
@RestController
public class userController {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminStatsService adminStatsService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());
            if (authenticatedUser != null) {
                return ResponseEntity.ok(authenticatedUser);
            } else {
                return ResponseEntity.badRequest().body("Invalid username or password. Please try again.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }



    @PostMapping("/user")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/stats")
    public ResponseEntity<AdminStats> getAdminStats() {
        try {
            AdminStats adminStats = adminStatsService.getAdminStats();
            return ResponseEntity.ok(adminStats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body("Email parameter is required.");
            }

            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                userService.createPasswordResetToken(userOptional.get());
                return ResponseEntity.ok("Password reset link has been sent to your email.");
            } else {
                return ResponseEntity.badRequest().body("User with given email not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            Optional<User> userOptional = userService.findByResetToken(token);
            if (userOptional.isPresent() && userOptional.get().getResetTokenExpiration().isAfter(LocalDateTime.now())) {
                userService.updatePassword(userOptional.get(), newPassword);
                return ResponseEntity.ok("Password has been reset successfully.");
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired reset token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }
}

