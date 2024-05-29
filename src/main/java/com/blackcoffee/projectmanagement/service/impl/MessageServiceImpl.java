package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.dto.*;
import com.blackcoffee.projectmanagement.entity.Chat;
import com.blackcoffee.projectmanagement.entity.Message;
import com.blackcoffee.projectmanagement.entity.User;
import com.blackcoffee.projectmanagement.exception.ProjectAPIException;
import com.blackcoffee.projectmanagement.exception.ResourceNotFoundException;
import com.blackcoffee.projectmanagement.repository.ChatRepository;
import com.blackcoffee.projectmanagement.repository.MessageRepository;
import com.blackcoffee.projectmanagement.repository.UserRepository;
import com.blackcoffee.projectmanagement.service.MessageService;
import com.blackcoffee.projectmanagement.utils.AESEncryptionDecryption;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ChatRepository chatRepository;

    @Value("${message.secret.key}")
    private String secretKey;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public MessageDto sendMessage(Long userId, Long chatId, String content) {
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id",userId));
        Chat chat=chatRepository.findById(chatId).orElseThrow(()-> new ResourceNotFoundException("Chat", "id", chatId));
        Message message= new Message();
        System.out.println(secretKey);
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        String encryptedString = aesEncryptionDecryption.encrypt(content, secretKey);

        message.setContent(encryptedString);
        message.setSender(user);
        message.setCreatedAt(LocalDate.now());
        message.setChat(chat);
        Message savedMessage=messageRepository.save(message);

        return mapToDto(savedMessage);
    }



    @Override
    public List<MessageByChatResponse> getMessageByChat(Long projectId) {
        List<MessageByChat> messageByChats=messageRepository.findAllMessagesByChatId(projectId);
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        List<MessageByChatResponse> messageByChatResponseList= new ArrayList<>();
        for(int i =0; i<messageByChats.size(); i++){
            MessageByChatResponse message= new MessageByChatResponse();
            message.setId(messageByChats.get(i).getId());
            message.setName(messageByChats.get(i).getName());
            String decryptedString = aesEncryptionDecryption.decrypt(messageByChats.get(i).getContent(), secretKey);
            message.setContent(decryptedString);
            message.setProjectId(messageByChats.get(i).getProject_Project_Id());
            message.setCreatedAt(messageByChats.get(i).getCreated_At());
            message.setSenderId(messageByChats.get(i).getSender_User_Id());
           messageByChatResponseList.add(message);


        }


        return messageByChatResponseList;

    }
    private MessageDto mapToDto(Message savedMessage) {
        MessageDto messageDto= new MessageDto();
        messageDto.setId(savedMessage.getId());
        String messageEncrypted= savedMessage.getContent();

        System.out.println(secretKey);
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        String decryptedString = aesEncryptionDecryption.decrypt(messageEncrypted, secretKey);

        messageDto.setContent(decryptedString);
        messageDto.setCreatedAt(savedMessage.getCreatedAt());
        ChatDto chatDto= new ChatDto();
        chatDto.setChatId(savedMessage.getChat().getId());
        chatDto.setName(savedMessage.getChat().getName());
        chatDto.setProjectId(savedMessage.getChat().getProject().getProjectId());
        messageDto.setChat(chatDto);
        UserDto userDto= new UserDto();
        userDto.setEmail(savedMessage.getSender().getEmail());
        userDto.setUserId(savedMessage.getSender().getUserId());
        userDto.setFullName(savedMessage.getSender().getFullName());
        userDto.setProjectSize(savedMessage.getSender().getProjectSize());
        messageDto.setSender(userDto);
        return messageDto;
    }
}
