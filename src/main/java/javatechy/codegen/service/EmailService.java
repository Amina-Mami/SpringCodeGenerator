package javatechy.codegen.service;

public interface EmailService {
   void sendPasswordResetEmail(String email, String resetLink);


}
