package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.LoginRequest;
import com.blackcoffee.projectmanagement.dto.RegisterDto;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.service.AuthService;
import com.blackcoffee.projectmanagement.service.SubcriptionService;
import com.blackcoffee.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private SubcriptionService subcriptionService;
    @Autowired
    private UserService userService;

    @PostMapping(value={"/signup", "/register"})
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterDto user) {
        AuthResponse authResponse =authService.createUser(user);

        UserDto userDto= new UserDto();
        userDto.setUserId(authResponse.getId());
        userDto.setEmail(authResponse.getEmail());
        userDto.setFullName(authResponse.getFullName());
        subcriptionService.createSubcription(userDto);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping(value={"/signin", "/login"})
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserProfileById(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }
}
