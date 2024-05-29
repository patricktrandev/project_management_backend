package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.*;
import com.blackcoffee.projectmanagement.service.MessageService;
import com.blackcoffee.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @PostMapping("/messages/send")
    public ResponseEntity<MessageDto> sendMessage(@RequestHeader("Authorization") String token,@RequestBody MessageRequest messageRequest){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return new ResponseEntity<>(messageService.sendMessage(loggedInUser.getId(),messageRequest.getId(),messageRequest.getContent()), HttpStatus.CREATED);
    }


    @GetMapping("/projects/{id}/messages")
    public ResponseEntity<List<MessageByChatResponse>> sendMessage(@RequestHeader("Authorization") String token, @PathVariable Long id){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        return ResponseEntity.ok(messageService.getMessageByChat(id));
    }

}
