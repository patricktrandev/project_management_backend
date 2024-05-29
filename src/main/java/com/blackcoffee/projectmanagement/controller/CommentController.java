package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.CommentDto;
import com.blackcoffee.projectmanagement.dto.CommentRequest;
import com.blackcoffee.projectmanagement.service.CommentService;
import com.blackcoffee.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @PostMapping("/issues/{id}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("id") Long id,
                                                    @RequestHeader("Authorization") String token,
                                                    @RequestBody CommentRequest commentRequest){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return new ResponseEntity<>(commentService.createComment(id, loggedInUser.getId(), commentRequest.getComment()), HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        commentService.deleteComment(id, loggedInUser.getId());
        return ResponseEntity.ok("Delete Comment Successfully");
    }

    @GetMapping("/issues/{id}/comments")
    public ResponseEntity<List<CommentDto>> findCommentByIssue(@PathVariable("id") Long id){

        return ResponseEntity.ok(commentService.findCommentByIssue(id));
    }
}
