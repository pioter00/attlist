package com.root.attendancelist.service;

import com.root.attendancelist.model.User;
import com.root.attendancelist.repository.UserRepository;
import com.root.attendancelist.service.interfaces.UserServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    public UserService(UserRepository studentRepository) {
        this.userRepository = studentRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User saveUser(User student) {
        return userRepository.save(student);
    }

    @Override
    public User updateUser(User student) {
        return userRepository.save(student);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
