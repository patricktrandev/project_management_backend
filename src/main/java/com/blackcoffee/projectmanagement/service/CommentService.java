package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.dto.CommentDto;
import com.blackcoffee.projectmanagement.entity.Comment;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long issueId, Long userId, String comment);
    void deleteComment(Long commentId, Long userId);
    List<CommentDto> findCommentByIssue(Long issueId);



}
