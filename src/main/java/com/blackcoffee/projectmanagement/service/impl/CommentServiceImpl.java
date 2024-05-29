package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.CommentDto;
import com.blackcoffee.projectmanagement.dto.IssueDto;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.entity.Comment;
import com.blackcoffee.projectmanagement.entity.Issue;
import com.blackcoffee.projectmanagement.entity.User;
import com.blackcoffee.projectmanagement.exception.ProjectAPIException;
import com.blackcoffee.projectmanagement.exception.ResourceNotFoundException;
import com.blackcoffee.projectmanagement.repository.CommentRepository;
import com.blackcoffee.projectmanagement.repository.IssueRepository;
import com.blackcoffee.projectmanagement.repository.UserRepository;
import com.blackcoffee.projectmanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private IssueRepository issueRepository;
    private UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, IssueRepository issueRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(Long issueId, Long userId, String comment) {
        Issue issue=issueRepository.findById(issueId).orElseThrow(()-> new ResourceNotFoundException("Issue","id",issueId));
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id",userId));
        Comment newComment= new Comment();
        newComment.setContent(comment);
        newComment.setCreatedDate(LocalDate.now());
        newComment.setIssue(issue);
        newComment.setUser(user);
        Comment savedComment=commentRepository.save(newComment);



        return mapToDto(savedComment);
    }



    @Override
    public void deleteComment(Long commentId, Long userId) {

        Comment comment=commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if(comment.getUser().getUserId()!=userId){
            throw new ProjectAPIException(HttpStatus.FORBIDDEN, "User does not have permission to delete comment");

        }
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDto> findCommentByIssue(Long issueId) {
        List<Comment> comments=commentRepository.findByIssueIssueId(issueId);
        List<CommentDto> commentDtoList= new ArrayList<>();
        for(Comment c: comments){
            commentDtoList.add(mapToDto(c));

        }
        return commentDtoList;
    }

    private CommentDto mapToDto(Comment newComment) {
        CommentDto commentDto= new CommentDto();
        commentDto.setId(newComment.getId());
        commentDto.setContent(newComment.getContent());
        commentDto.setCreatedDate(newComment.getCreatedDate());
        UserDto userDto= new UserDto();
        userDto.setEmail(newComment.getUser().getEmail());
        userDto.setUserId(newComment.getUser().getUserId());
        userDto.setFullName(newComment.getUser().getFullName());
        userDto.setProjectSize(newComment.getUser().getProjectSize());
        commentDto.setUser(userDto);
        IssueDto issueDto= new IssueDto();
        issueDto.setTitle(newComment.getIssue().getTitle());
        issueDto.setDescription(newComment.getIssue().getDescription());
        issueDto.setPriority(newComment.getIssue().getPriority());
        issueDto.setStatus(newComment.getIssue().getStatus());
        issueDto.setDueDate(newComment.getIssue().getDueDate());
        issueDto.setProjectId(newComment.getIssue().getProjectId());
        issueDto.setId(newComment.getIssue().getIssueId());
        List<UserDto> assignedUser= new ArrayList<>();
        for(User foundIssue:newComment.getIssue().getAssignee()){
            UserDto user= new UserDto();
            user.setUserId(foundIssue.getUserId());
            user.setEmail(foundIssue.getEmail());
            user.setFullName(foundIssue.getFullName());
            user.setProjectSize(foundIssue.getProjectSize());
            assignedUser.add(userDto);
        }

        issueDto.setAssignee(assignedUser);

        commentDto.setIssue(issueDto);
        return commentDto;
    }
}
