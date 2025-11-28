package com.team_proj.dsfw_team_proj.auth.service;

import com.team_proj.dsfw_team_proj.auth.entity.User;
import com.team_proj.dsfw_team_proj.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User validateUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}