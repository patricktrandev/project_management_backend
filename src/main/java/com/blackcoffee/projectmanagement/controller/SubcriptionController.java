package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.SubcriptionDto;
import com.blackcoffee.projectmanagement.dto.SubcriptionRequest;
import com.blackcoffee.projectmanagement.entity.PlanType;

import com.blackcoffee.projectmanagement.service.SubcriptionService;
import com.blackcoffee.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcriptions")
public class SubcriptionController {
    @Autowired
    private UserService userService;
    @Autowired
    private SubcriptionService subcriptionService;

    @GetMapping
    public ResponseEntity<SubcriptionDto> getUserSubcription(@RequestHeader("Authorization") String token){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);

        return ResponseEntity.ok(subcriptionService.getUserSubcription(loggedInUser.getId()));
    }

    @PutMapping
    public ResponseEntity<SubcriptionDto> upgradeSubcription(@RequestHeader("Authorization") String token, @RequestBody SubcriptionRequest subcriptionRequest){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        PlanType currentPlan= PlanType.FREE;

        if(subcriptionRequest.getPlanType().toUpperCase().contains(PlanType.MONTHLY.name())){
            currentPlan= PlanType.MONTHLY;
        }else if (subcriptionRequest.getPlanType().toUpperCase().contains(PlanType.ANNUALLY.name())){
            currentPlan= PlanType.ANNUALLY;
        }
        //System.out.println(currentPlan);
        subcriptionService.upgradeSubcription(loggedInUser.getId(), currentPlan);
        return new ResponseEntity<>(subcriptionService.getUserSubcription(loggedInUser.getId()), HttpStatus.OK);
    }
}
