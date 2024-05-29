package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.config.JwtProvider;
import com.blackcoffee.projectmanagement.dto.*;
import com.blackcoffee.projectmanagement.entity.User;
import com.blackcoffee.projectmanagement.exception.ProjectAPIException;
import com.blackcoffee.projectmanagement.exception.ResourceNotFoundException;
import com.blackcoffee.projectmanagement.repository.UserRepository;
import com.blackcoffee.projectmanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;




    @Override
    public AuthResponse createUser(RegisterDto user)  {
        User isUserEmailExist= userRepository.findByEmail(user.getEmail());
        if(isUserEmailExist!=null){
            throw  new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR,"Email already exist with another account!");
        }

        User createdUser= new User();
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());

        User newUser= userRepository.save(createdUser);

        Authentication authentication= new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken= JwtProvider.generateToken(authentication);
        return mapToDto(newUser, jwtToken);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        String email= loginRequest.getEmail();
        String password= loginRequest.getPassword();
        Authentication authentication= validateAuthentication(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken= JwtProvider.generateToken(authentication);
        AuthResponse authResponse= new AuthResponse();
        authResponse.setEmail(email);
        authResponse.setToken(jwtToken);
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found  with email");
        }
        authResponse.setFullName(user.getFullName());
        authResponse.setMessage("Login successfully");
        return authResponse;
    }



    private Authentication validateAuthentication(String email, String password) {
        UserDetails userDetails= customUserDetailsService.loadUserByUsername(email);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid Email");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private AuthResponse mapToDto(User user, String jwtToken){
        AuthResponse authResponse= new AuthResponse();
        authResponse.setId(user.getUserId());
        authResponse.setEmail(user.getEmail());
        authResponse.setToken(jwtToken);
        authResponse.setFullName(user.getFullName());
        authResponse.setMessage("Register successfully");
        return authResponse;
    }
}
