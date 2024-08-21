package com.banking.repository;

import com.banking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameContaining(String name);  // Fix: changed findByNameContaining to findByUsernameContaining
    List<User> findByEmailContaining(String email);
    List<User> findByPhonenumberContaining(String phonenumber);  // Fix: match with the field name in the User model
}
