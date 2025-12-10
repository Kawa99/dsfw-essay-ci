package com.team_proj.dsfw_team_proj.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserEntity userEntity) {
        if (userRepository.findByEmail(userEntity.getEmail()) != null) {
            throw new RuntimeException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userEntity.setRole("USER");
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}