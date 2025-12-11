package com.team_proj.dsfw_team_proj.auth;

public interface UserService {
    UserEntity save(UserEntity userEntity);
    UserEntity findByEmail(String email);
    UserEntity getCurrentUser();
}