package com.blackcoffee.projectmanagement.controller;

import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.ResetPasswordRequest;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.exception.ProjectAPIException;
import com.blackcoffee.projectmanagement.service.InvitationService;
import com.blackcoffee.projectmanagement.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;



    @PostMapping("/forgot_password")
    public ResponseEntity<String> verifyEmail(@RequestHeader("Authorization") String token){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        try{
            userService.resetPassword(loggedInUser.getEmail());
        }catch (MessagingException e){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR,"Messaging Exception: "+e.getMessage());
        }

        return new ResponseEntity<>("Email sent for verification", HttpStatus.OK);
    }

    @PostMapping("/verify/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@RequestHeader("Authorization") String token, @PathVariable String email, @PathVariable Long otp){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        userService.verifyOtp(otp,email);

        return new ResponseEntity<>("OTP verified", HttpStatus.OK);
    }

    @PostMapping("/forgot_password/reset")
    public ResponseEntity<UserDto> changePassword(@RequestHeader("Authorization") String token, @RequestBody ResetPasswordRequest resetPasswordRequest){
        AuthResponse loggedInUser= userService.findUserProfileByJwt(token);
        userService.deleteRecordResetPassword(loggedInUser.getId());
        return new ResponseEntity<>(userService.updateNewPassword(resetPasswordRequest, loggedInUser.getId()), HttpStatus.OK);
    }


}
