package com.blackcoffee.projectmanagement.dto;

import com.blackcoffee.projectmanagement.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private Long chatId;
    private String name;
    private Long projectId;

}
