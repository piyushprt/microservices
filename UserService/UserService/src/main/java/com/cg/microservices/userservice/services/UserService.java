package com.cg.microservices.userservice.services;

import com.cg.microservices.userservice.entities.User;

import java.util.List;

public interface UserService {
    //create
    User createUser(User user);

    //get all users
    List<User> getAllUser();

    //get single user of given Id
    User getUser(String userId);
}
