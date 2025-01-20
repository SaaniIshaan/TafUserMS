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
    private final RestTemplate restTemplate;

    @Autowired
    private final UserServiceImpl userServiceImpl;

    @Value("${datastore.ms.url}")
    String dataStoreServiceUrl;

    public UserController(UserServiceImpl userServiceImpl, RestTemplate restTemplate) {
        this.userServiceImpl = userServiceImpl;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers(){
       return new ResponseEntity<List<Users>>(userServiceImpl.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        String url = dataStoreServiceUrl + "/users";
        Users createdUser = restTemplate.postForObject(url, user, Users.class);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUser(@PathVariable Long userId) {
        String url = dataStoreServiceUrl + "/users/" + userId;
        ResponseEntity<Users> response = restTemplate.getForEntity(url, Users.class);
        return ResponseEntity.ok(response.getBody());
    }


//    @PutMapping("/{userId}")
 //   public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody Users user) {
 //       String url = dataStoreServiceUrl + "/users/" + userId;
 //       restTemplate.put(url, user);
//       return ResponseEntity.noContent().build();
//    }

    @PutMapping
    public ResponseEntity<Users> updateUser(@RequestBody Users updatedUser) {
        Users updatedResponse = userServiceImpl.updateUser(updatedUser);
        if (updatedResponse != null) {
            return ResponseEntity.ok(updatedResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @ExceptionHandler
    public ResponseEntity<String> respondWithError(Exception e) {
        logger.error("Exception Occurred. Details : {}", e.getMessage());
        return new ResponseEntity<>("Exception Occurred. More Info :"
                + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
