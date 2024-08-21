package com.banking.Service;

import com.banking.model.User;
import com.banking.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserServiceProvider {
    public String addUser(UserDto user);
    public String loginUser(UserDto user);
    public String logoutUser(UserDto user);
    public String forgetPassword(UserDto user);
    public String changePassword(String email, String password);
    public List<User> getAllUsers();
    public Optional<User> getUserById(Long id);
    public List<User> searchByName(String name);
    public List<User> searchByEmail(String email);
    public List<User> searchByPhoneNumber(String phoneNumber);
    public void deleteUserById(Long id);

}
