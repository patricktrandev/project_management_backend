package com.blackcoffee.projectmanagement.dto;

import com.blackcoffee.projectmanagement.entity.Issue;
import com.blackcoffee.projectmanagement.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private LocalDate createdDate;
    private UserDto user;
    private IssueDto issue;
}
