package com.blackcoffee.projectmanagement.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmailInvitation(String userEmail, String url) throws MessagingException;

    void sendEmailResetPassword(String userEmail, Long otp)throws MessagingException;
}
