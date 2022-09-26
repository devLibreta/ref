package com.example.ref.security_jwt.service;

import com.example.ref.security_jwt.model.User;
import com.example.ref.security_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {
    private final UserRepository userRepository;

    @Autowired
    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User findById(int id){
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
        }
}
