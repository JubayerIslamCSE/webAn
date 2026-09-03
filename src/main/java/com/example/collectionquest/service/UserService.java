package com.example.collectionquest.service;

import com.example.collectionquest.model.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User findByEmail(String email);
    void save(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> getAll();
}