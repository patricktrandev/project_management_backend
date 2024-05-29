package com.blackcoffee.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    Long projectId;
    String category;
    String description;
    String name;
    List<String> tags;
    AuthResponse owner;
    ChatDto chat;
    List<UserByProject> teams;

}
