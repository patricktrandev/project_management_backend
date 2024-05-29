package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.IssueStatusRequest;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.IssueDto;
import com.blackcoffee.projectmanagement.entity.Issue;
import com.blackcoffee.projectmanagement.service.IssueService;
import com.blackcoffee.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IssueController {

    @Autowired
    private IssueService issueService;
    @Autowired
    private UserService userService;

    @GetMapping("/issues/{id}")
    public ResponseEntity<IssueDto> getIssueById(@PathVariable("id") Long id,@RequestHeader("Authorization") String token){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return ResponseEntity.ok(issueService.getIssueById(id));
    }
    @GetMapping("/projects/{id}/issues")
    public ResponseEntity<List<IssueDto>> getIssueByProjectId(@PathVariable("id") Long id,
                                                           @RequestHeader("Authorization") String token){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return ResponseEntity.ok(issueService.getIssueByProjectId(id));
    }
    @PostMapping("/issues")
    public ResponseEntity<IssueDto> createIssue(@RequestBody IssueDto issueDto,
                                             @RequestHeader("Authorization") String token){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        UserDto userDto =userService.findUserById(loggedInUser.getId());
        IssueDto createIssue= issueService.createIssue(userDto.getUserId(),issueDto);
        return new ResponseEntity<>(createIssue, HttpStatus.CREATED);
    }

    @DeleteMapping("/issues/{id}")
    public ResponseEntity<String> deleteIssue(@PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        issueService.deleteIssue(id, loggedInUser.getId());
        return ResponseEntity.ok("Delete issue successfully");
    }

    // search issue

    @PutMapping("/issues/{id}/assignee/{userId}")
    public ResponseEntity<IssueDto> addUserToIssue(@PathVariable("id") Long issueId,@RequestHeader("Authorization") String token, @PathVariable("userId") Long userId){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);

        return new ResponseEntity<>(issueService.addUserToIssue(issueId, userId), HttpStatus.OK);
    }

    @PutMapping("/issues/{id}")
    public ResponseEntity<IssueDto> updateIssueStatus(@PathVariable("id") Long id,@RequestHeader("Authorization") String token,@RequestBody IssueStatusRequest status){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return new ResponseEntity<>(issueService.updateIssueStatus(id,status), HttpStatus.OK);
    }



}
