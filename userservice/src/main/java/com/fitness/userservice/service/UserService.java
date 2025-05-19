package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public UserResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            User existingUser = userRepository.findByEmail(registerRequest.getEmail());
            UserResponse userResponse= new UserResponse();
            userResponse.setId(existingUser.getUserId());
            userResponse.setKeycloakId(existingUser.getKeycloakId());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName((existingUser.getLastName()));
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());
            return userResponse;

        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setKeycloakId(registerRequest.getKeycloakId());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName((registerRequest.getLastName()));
        user.setPassword(registerRequest.getPassword());

        User savedUser = userRepository.save(user);
        UserResponse userResponse= new UserResponse();
        userResponse.setId(savedUser.getUserId());
        userResponse.setKeycloakId(savedUser.getKeycloakId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName((savedUser.getLastName()));
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
        return userResponse;

    }

    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        UserResponse userResponse= new UserResponse();
        userResponse.setId(user.getUserId());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName((user.getLastName()));
        userResponse.setPassword(user.getPassword());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

//    public Boolean existsByUserId(String userId) {
//        return userRepository.existsById(userId);
//    }

    public Boolean existsByKeycloakId(String keycloakId) { return userRepository.existsByKeycloakId(keycloakId);}
}
