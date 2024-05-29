package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.AllProjectsByUser;
import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.ProjectDto;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.service.ProjectService;
import com.blackcoffee.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<List<AllProjectsByUser>> getAllProjects(@PathVariable("id") Long userId,
                                                                  @RequestHeader("Authorization") String token){

        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return ResponseEntity.ok(projectService.getAllProjectsParticipatedByUserId(userId));
    }
}
