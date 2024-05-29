package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.ResetPasswordRequest;
import com.blackcoffee.projectmanagement.dto.UserDto;
import jakarta.mail.MessagingException;

public interface UserService {
    AuthResponse findUserProfileByJwt(String jwt);
    UserDto findUserByEmail(String email);
    UserDto findUserById(Long userId);
    UserDto updateUserProjectSize(UserDto user, int number);

    void resetPassword(String email) throws MessagingException;

    void verifyOtp(Long otp, String email);
    UserDto updateNewPassword(ResetPasswordRequest resetPasswordRequest, Long userId);
    void deleteRecordResetPassword(Long userId);

}
