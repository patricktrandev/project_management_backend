package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.config.JwtProvider;
import com.blackcoffee.projectmanagement.entity.Invitation;
import com.blackcoffee.projectmanagement.exception.ProjectAPIException;
import com.blackcoffee.projectmanagement.repository.InvitationRepository;
import com.blackcoffee.projectmanagement.service.EmailService;
import com.blackcoffee.projectmanagement.service.InvitationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvitationServiceImpl implements InvitationService {

    private InvitationRepository invitationRepository;
    private EmailService emailService;

    @Autowired
    public InvitationServiceImpl(InvitationRepository invitationRepository, EmailService emailService) {
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
    }

    @Override
    public void sendInvitationToUser(String email, Long projectId) throws MessagingException {
        String invitationToken= String.valueOf(UUID.randomUUID());
        Invitation invitation= new Invitation();
        invitation.setEmail(email);
        invitation.setProjectId(projectId);
        invitation.setToken(invitationToken);

        invitationRepository.save(invitation);
        String invitationLink="http://localhost:5454/api/projects/accept_invitation?token="+invitationToken;
        emailService.sendEmailInvitation(email,invitationLink);

    }

    @Override
    public Invitation acceptInvitation(String token ) {
        Invitation invitation= invitationRepository.findByToken(token);
        if(invitation==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Invitation");
        }
        return invitation;
    }

    @Override
    public String getTokenByUserEMail(String userEmail) {
        Invitation invitation= invitationRepository.findByEmail(userEmail);

        return invitation.getToken();
    }

    @Override
    public void deleteToken(String token) {
        Invitation invitation= invitationRepository.findByToken(token);
        if(invitation==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Invitation");
        }
        invitationRepository.delete(invitation);
    }
}
