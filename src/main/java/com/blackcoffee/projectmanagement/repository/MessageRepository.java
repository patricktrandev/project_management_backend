package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.dto.MessageByChat;
import com.blackcoffee.projectmanagement.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(nativeQuery = true, value = "SELECT c.id, c.name,c.project_project_id,m.content,m.created_at,m.sender_user_id\n " +
            "FROM chats as c\n " +
            "join messages as m on m.chat_id = c.id\n " +
            "where c.project_project_id=:id")
    List<MessageByChat> findAllMessagesByChatId(@Param("id") Long id);
}
