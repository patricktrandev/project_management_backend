package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.dto.MessageByChat;
import com.blackcoffee.projectmanagement.dto.MessageByChatResponse;
import com.blackcoffee.projectmanagement.dto.MessageDto;
import com.blackcoffee.projectmanagement.entity.Message;

import java.util.List;

public interface MessageService {
    MessageDto sendMessage(Long senderId, Long chatId, String content);

    List<MessageByChatResponse>  getMessageByChat(Long projectId);
}
