package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.config.JwtProvider;
import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.ResetPasswordRequest;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.dto.UserFindByOtpAndUserId;
import com.blackcoffee.projectmanagement.entity.ResetPassword;
import com.blackcoffee.projectmanagement.entity.User;
import com.blackcoffee.projectmanagement.exception.ProjectAPIException;
import com.blackcoffee.projectmanagement.exception.ResourceNotFoundException;
import com.blackcoffee.projectmanagement.repository.ResetPasswordRepository;
import com.blackcoffee.projectmanagement.repository.UserRepository;
import com.blackcoffee.projectmanagement.service.EmailService;
import com.blackcoffee.projectmanagement.service.UserService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private EmailService emailService;
    private ResetPasswordRepository resetPasswordRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,ModelMapper modelMapper,EmailService emailService,ResetPasswordRepository resetPasswordRepository,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.modelMapper= modelMapper;
        this.emailService=emailService;
        this.resetPasswordRepository=resetPasswordRepository;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    public AuthResponse findUserProfileByJwt(String jwt) {
        String email= JwtProvider.getEmail(jwt);
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found with email");
        }
        return mapToAuthResponse(user);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found with email");
        }
        return mapToDto(user);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id",userId));
        return mapToDto(user);
    }

    @Override
    public UserDto updateUserProjectSize(UserDto userDto, int number) {

        return null;
    }

    @Override
    public void resetPassword(String email) throws MessagingException {
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found with email");
        }
        Long otp=otpGenerator();
        ResetPassword resetPassword= new ResetPassword();
        resetPassword.setExpirationTime(LocalDateTime.now().plusMinutes(30));
        resetPassword.setOtp(otp);
        resetPassword.setUser(user);
        emailService.sendEmailResetPassword(user.getEmail(),otp);
        resetPasswordRepository.save(resetPassword);
    }

    @Override
    public void verifyOtp(Long otp, String email) {

        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found with email");
        }
        UserFindByOtpAndUserId resetPassword =resetPasswordRepository.findByOtpAndUser(otp, user.getUserId());
        if(resetPassword==null){
            throw new ProjectAPIException(HttpStatus.BAD_REQUEST, "Invalid OTP for email "+email);
        }


    }

    @Override
    public UserDto updateNewPassword(ResetPasswordRequest resetPasswordRequest, Long userId) {
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id",userId));
        if(!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())){
            throw new ProjectAPIException(HttpStatus.BAD_REQUEST, "Check confirm password");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        User updatedUser=userRepository.save(user);


        return mapToDto(updatedUser);
    }

    @Override
    public void deleteRecordResetPassword(Long userId) {
        ResetPassword resetPassword= resetPasswordRepository.findByUserUserId(userId);
        if(resetPassword==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR,"Server Reset Password get Error");
        }
        resetPasswordRepository.deleteByUserUserId(userId);
    }


    private Long otpGenerator(){
        Random random= new Random();
        return random.nextLong(100_000,999_999);
    }

    private UserDto mapToDto(User user) {

        return modelMapper.map(user, UserDto.class);
    }
    private AuthResponse mapToAuthResponse(User user) {

        return modelMapper.map(user, AuthResponse.class);
    }

}
