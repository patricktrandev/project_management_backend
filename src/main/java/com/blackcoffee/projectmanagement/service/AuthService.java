package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.dto.*;

public interface AuthService {
    AuthResponse createUser(RegisterDto registerDto) ;
    AuthResponse login(LoginRequest loginRequest);

}
