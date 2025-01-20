package com.tekarch.TafUserMS.services;

import com.tekarch.TafUserMS.Models.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;

    @Value("${datastore.ms.url}")
    String dataStoreServiceUrl;

    public UserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Users> getAllUsers() {
        Users[] users = restTemplate.getForObject(dataStoreServiceUrl + "/users", Users[].class);
        return Arrays.asList(users);
    }

    @Override
    public Users registerANewUser(Users user) {
        return restTemplate.postForObject(dataStoreServiceUrl + "/users", user, Users.class);
    }

    @Override
    public Users getUserById(Long userId) {
        return restTemplate.getForObject(dataStoreServiceUrl + "/users/" + userId, Users.class);
    }

 //   @Override
 //   public void updateUserDetails(Long userId, Users user) {
 //       restTemplate.put(dataStoreServiceUrl + "/users/" + userId, user);

 //   }

    @Override
    public Users updateUser(Users updatedUser) {
        String userUrl = dataStoreServiceUrl + "/users";
        // Make a put request to update the user in the datastore service
        Users updatedResponse = restTemplate.postForObject(userUrl, updatedUser, Users.class);
        if (updatedUser != null) {
            return updatedResponse;
        } else {
            throw new RuntimeException("Failed to update user with ID:" + updatedUser.getUserId());
        }
    }


}