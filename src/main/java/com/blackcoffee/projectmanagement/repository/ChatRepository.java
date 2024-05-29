package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
