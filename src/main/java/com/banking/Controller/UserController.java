package com.banking.Controller;

import com.banking.model.User;
import com.banking.Service.UserServiceProvider;
import com.banking.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserServiceProvider UserService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto user) {
        String result = UserService.addUser(user);
        if (result.equals("Registration successful!")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto user){
        String result = UserService.loginUser(user);
        if (result.equals("Login successful!")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestBody UserDto user){
        String result = UserService.logoutUser(user);
        if (result.equals("Logout successful!")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
    //-------------------
    @PostMapping("/forgetpassword")
    public ResponseEntity<String>forgetPassword(@RequestBody UserDto user){
        String fp=UserService.forgetPassword(user);
        return new ResponseEntity<>(fp, HttpStatus.CREATED);
    }
    @PostMapping("/changepassword")
    public ResponseEntity<String>changePassword(@RequestParam String email,@RequestParam String password){
        String fp=UserService.changePassword(email,password);
        return new ResponseEntity<>(fp, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = UserService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = UserService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchByField(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber) {

        List<User> users;
        if (name != null) {
            users = UserService.searchByName(name);
        } else if (email != null) {
            users = UserService.searchByEmail(email);
        } else if (phoneNumber != null) {
            users = UserService.searchByPhoneNumber(phoneNumber);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void DeleteUserById(@PathVariable Long id){
         UserService.deleteUserById(id);
    }




}