package com.tekarch.TafUserMS.services;

import com.tekarch.TafUserMS.Models.Users;

import java.util.List;


public interface UserService {

    List<Users> getAllUsers();
    Users registerANewUser(Users user);
    Users getUserById(Long userId);
 //   void updateUserDetails(Long userId ,Users user);
    Users updateUser(Users user);

}
