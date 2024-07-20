package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Override
    public void sendEmailInvitation(String userEmail, String url) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,"utf-8");

        String subject="Join Project Team Invitation";
        String text="Click the link to join team project \n"+url;
        helper.setFrom(sender);
        helper.setTo(userEmail);
        helper.setSubject(subject);
        helper.setText(text, true);

        try{
            javaMailSender.send(mimeMessage);
        }catch(MailSendException e){
            System.out.println(e.getMessage());
            throw new MailSendException("Failed to send email");
        }
    }

    @Override
    public void sendEmailResetPassword(String userEmail, Long otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,"utf-8");

        String subject="Reset Password | Project Management";
        String text="Your otp: \n"+otp;
        helper.setFrom(sender);
        helper.setTo(userEmail);
        helper.setSubject(subject);
        helper.setText(text, true);

        try{
            javaMailSender.send(mimeMessage);
        }catch(MailSendException e){
            System.out.println(e.getMessage());
            throw new MailSendException("Failed to send email");
        }
    }



}
