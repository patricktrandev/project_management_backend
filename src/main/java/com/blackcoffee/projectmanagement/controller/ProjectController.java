package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.InvitationDto;
import com.blackcoffee.projectmanagement.dto.ProjectDto;

import com.blackcoffee.projectmanagement.entity.Invitation;
import com.blackcoffee.projectmanagement.entity.Project;
import com.blackcoffee.projectmanagement.service.InvitationService;
import com.blackcoffee.projectmanagement.service.ProjectService;
import com.blackcoffee.projectmanagement.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private InvitationService invitationService;

    @GetMapping()
    public ResponseEntity<List<ProjectDto>> searchProjectByKeyword( @RequestHeader("Authorization") String token,@RequestParam(required = false) Map<String, String> params){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return ResponseEntity.ok(projectService.searchProjectsByCondition(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getSingleProjectById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        ProjectDto projectDto= projectService.getProjectById(id);
        return ResponseEntity.ok(projectDto);
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto, @RequestHeader("Authorization") String token){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);

        return new ResponseEntity<>(projectService.createProject(projectDto, loggedInUser), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@RequestBody ProjectDto projectDto, @RequestHeader("Authorization") String token,
                                                    @PathVariable("id") Long id){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return new ResponseEntity<>(projectService.updateProject(projectDto,id,loggedInUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@RequestHeader("Authorization") String token,@PathVariable("id") Long id){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        projectService.deleteProject(id, loggedInUser);
        return ResponseEntity.ok("Delete Project Successfully");
    }

    //search project /api/projects/search?
    //@GetMapping

    //get Chat By Project Id
    //@GetMapping("/{id}/chat")

    @PostMapping("/invite")
    public ResponseEntity<String> inviteUserToProject(@RequestBody InvitationDto invitationDto, @RequestHeader("Authorization") String token) throws MessagingException {
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        invitationService.sendInvitationToUser(invitationDto.getEmail(), invitationDto.getProjectId());
        return new ResponseEntity<>("User invitation sent", HttpStatus.OK);
    }
    @GetMapping("/accept_invitation")
    public ResponseEntity<Invitation> acceptInvitationProject(@RequestParam String token, @RequestHeader("Authorization") String jwt) {
        AuthResponse loggedInUser= userService.findUserProfileByJwt(jwt);
        Invitation invitation=invitationService.acceptInvitation(token);
        projectService.addUserToProject(invitation.getProjectId(), invitation.getEmail());
        return new ResponseEntity<>(invitation, HttpStatus.ACCEPTED);
    }
}
