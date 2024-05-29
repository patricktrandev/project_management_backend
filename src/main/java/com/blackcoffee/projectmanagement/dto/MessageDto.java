package com.blackcoffee.projectmanagement.dto;

import com.blackcoffee.projectmanagement.entity.Chat;
import com.blackcoffee.projectmanagement.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private String content;
    private LocalDate createdAt;
    private ChatDto chat;
    private UserDto sender;
}
