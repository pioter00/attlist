package com.root.attendancelist.service.interfaces;

import com.root.attendancelist.model.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    List<User> findAllUsers();
    Optional<User> findById(Long id);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
}
