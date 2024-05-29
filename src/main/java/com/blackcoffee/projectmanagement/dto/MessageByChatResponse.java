package com.blackcoffee.projectmanagement.dto;

import com.blackcoffee.projectmanagement.entity.Project;
import com.blackcoffee.projectmanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageByChatResponse {
    public Long id;
    public String name;
    public int projectId;
    public String content;
    public Date createdAt;
    public int senderId;
}
