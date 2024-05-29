package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.dto.ChatDto;
import com.blackcoffee.projectmanagement.entity.Chat;
import com.blackcoffee.projectmanagement.repository.ChatRepository;
import com.blackcoffee.projectmanagement.repository.ProjectRepository;
import com.blackcoffee.projectmanagement.service.ChatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private ChatRepository chatRepository;
    private ProjectRepository projectRepository;
    private ModelMapper mapper;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository, ProjectRepository projectRepository, ModelMapper mapper) {
        this.chatRepository = chatRepository;
        this.projectRepository = projectRepository;
        this.mapper = mapper;
    }


    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }
}
