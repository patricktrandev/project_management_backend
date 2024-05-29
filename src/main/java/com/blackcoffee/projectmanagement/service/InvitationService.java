package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.entity.Invitation;
import jakarta.mail.MessagingException;

public interface InvitationService {

    void sendInvitationToUser(String email, Long projectId) throws MessagingException;
    Invitation acceptInvitation(String token);
    String getTokenByUserEMail(String userEmail);
    void deleteToken(String token);
}
