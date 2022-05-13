package org.acme.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.acme.model.User;
import org.acme.service.UserService;

@ApplicationScoped
public class InMemoryUserService implements UserService {

    private static Map<String, User> usersMap = new HashMap<String, User>();

    @Override
    public User getUser(String id) {
        if (usersMap.isEmpty()) return null;
        User result = usersMap.get(id);
        return result;
    }

    @Override
    public boolean addUser(User user) {
        usersMap.put(user.getId(), user);
        return true;
    }

    @Override
    public boolean updateUser(String id, User user) {
        User oldUser = usersMap.get(id);
        if (oldUser == null) return false;
        usersMap.put(id, user);
        return true;
    }

    @Override
    public boolean deleteUser(String id) {
        if (usersMap.isEmpty()) return false;
        User result = usersMap.remove(id);
        if (result == null) return false;
        else return true; 
    }

    @Override
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<User>();
        for (User u : usersMap.values()) {
            usersList.add(u);
        }
        return usersList;
    }
    
}
