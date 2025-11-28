package com.team_proj.dsfw_team_proj.auth.service;

import com.team_proj.dsfw_team_proj.auth.entity.User;

public interface UserService {
    User save(User user);

    User validateUser(String email, String password);

    User findByEmail(String email);
}
