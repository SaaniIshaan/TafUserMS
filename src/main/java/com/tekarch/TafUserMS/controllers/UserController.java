package com.tekarch.TafUserMS.controllers;

import com.tekarch.TafUserMS.Models.Users;
import com.tekarch.TafUserMS.configs.RestTemplateConfig;
import com.tekarch.TafUserMS.services.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Value("${datastore.ms.url}")
    String dataStoreServiceUrl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers(){
       return new ResponseEntity<List<Users>>(userServiceImpl.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        return new ResponseEntity<>(userServiceImpl.registerANewUser(user),HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUser(@PathVariable Long userId) {
        Users user = userServiceImpl.getUserById(userId);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();

    }

    @PutMapping
    public ResponseEntity<Users> updateUser(@RequestBody Users updatedUser) {
        Users updatedResponse = userServiceImpl.updateUser(updatedUser);
        if (updatedResponse != null) {
            return ResponseEntity.ok(updatedResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/userId")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userServiceImpl.deleteUser(userId);
        String message = "User with ID" + userId + " has been successfully deleted";
        return ResponseEntity.ok(message);

    }

    @ExceptionHandler
    public ResponseEntity<String> respondWithError(Exception e) {
        logger.error("Exception Occurred. Details : {}", e.getMessage());
        return new ResponseEntity<>("Exception Occurred. More Info :"
                + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
