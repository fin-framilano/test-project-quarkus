package org.acme.service;

import java.util.List;

import org.acme.model.User;

public interface UserService {

    
    User getUser(String id);
    
    boolean addUser(User user);
    
    boolean updateUser(String id, User user);
    
    boolean deleteUser(String id);
    
    List<User> getAllUsers();
}
