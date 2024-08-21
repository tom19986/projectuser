package com.banking.Service;

import com.banking.emailservice.EmailService;
import com.banking.model.User;
import com.banking.dto.UserDto;
import com.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceProvider {

    @Autowired
   private UserRepository userRepository;
    @Autowired
    private EmailService emailservice;

    private RequestValidator valid = new RequestValidator();  // Consider @Autowired if it's a Spring bean

    @Override
    public String addUser(UserDto user) {
        User u = new User(); // Create a User entity object

        // Basic validation
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return "Username is required.";
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return "Email is required.";
        }
        if (!valid.isValidEmail(user.getEmail())) {
            return "Invalid email format.";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Password is required.";
        }

        List<User> usersByName = searchByName(user.getUsername());
        if (!usersByName.isEmpty()) {
            return "Username already exists.";
        }

        List<User> usersByEmail = searchByEmail(user.getEmail());
        if (!usersByEmail.isEmpty()) {
            return "Email already exists.";
        }

        if (!valid.isValidPassword(user.getPassword())) {
            return "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character.";
        }

        // Set user details
        u.setUsername(user.getUsername());
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        u.setPhonenumber(user.getPhonenumber());

        userRepository.save(u); // Save user to the database
        return "Registration successful!";
    }

    @Override
    public String loginUser(UserDto user) {
        List<User> users = searchByName(user.getUsername());

        if (users.isEmpty()) {
            return "User doesn't exist.";
        }

        User u = users.get(0);
        if (!u.getPassword().equals(user.getPassword())) {
            return "Password mismatched.";
        }

        Optional<User> userExist = userRepository.findById(u.getId());
        if (userExist.isPresent()) {
            u.setActive(true); // Update the active status
            userRepository.save(u); // Save the updated user object back to the database
            return "Login successful!";
        }
        return "Login failed.";
    }

    @Override
    public String logoutUser(UserDto user) {
        List<User> users = searchByName(user.getUsername());

        if (users.isEmpty()) {
            return "User doesn't exist.";
        }

        User u = users.get(0);
        Optional<User> userExist = userRepository.findById(u.getId());
        if (userExist.isPresent()) {
            u.setActive(false); // Update the active status
            userRepository.save(u); // Save the updated user object back to the database
            return "Logout successful!";
        }
        return "Logout failed.";
    }

    @Override
    public String forgetPassword(UserDto user) {
        String baseURL = "http://localhost:8080/api/user/changepassword?email=";
        List<User> users = searchByEmail(user.getEmail());

        if (users.isEmpty()) {
            return "Enter correct email ID.";
        }

        User u = users.get(0);
        if (!u.getEmail().equals(user.getEmail())) {
            return "Enter correct email ID.";
        }
        emailservice.sendEmail(u.getEmail(),"password reset",baseURL+u.getEmail()+"&password=");

        return "password reset link has sent to your email";
    }

    @Override
    public String changePassword(String email, String password) {
        List<User> users = searchByEmail(email);

        if (users.isEmpty()) {
            return "Enter correct email ID.";
        }

        User u = users.get(0);
        if (!u.getEmail().equals(email)) {
            return "Enter correct email ID.";
        }
        if (u.getPassword().equals(password)) {
            return "Enter new password";
        }

        Optional<User> userExist = userRepository.findById(u.getId());
        if (userExist.isPresent()) {
            u.setPassword(password);
            userRepository.save(u);
            return "Password updated successfully.";
        } else {
            return "User not found.";
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> searchByName(String name) {
        return userRepository.findByUsernameContaining(name);  // Fix: match method name in the repository
    }

    @Override
    public List<User> searchByEmail(String email) {
        return userRepository.findByEmailContaining(email);
    }

    @Override
    public List<User> searchByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhonenumberContaining(phoneNumber);  // Fix: match method name in the repository
    }
    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            throw new RuntimeException("User with id: " + id+" deleted successfully");
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
