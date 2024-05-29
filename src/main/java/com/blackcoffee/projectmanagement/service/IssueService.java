package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.dto.IssueDto;
import com.blackcoffee.projectmanagement.dto.IssueStatusRequest;
import com.blackcoffee.projectmanagement.entity.Issue;
import com.blackcoffee.projectmanagement.entity.User;

import java.util.List;

public interface IssueService {
    IssueDto getIssueById(Long issueId);
    List<IssueDto> getIssueByProjectId(Long projectId);
    IssueDto createIssue(Long userId, IssueDto issueDto);
   // Issue updateIssue(Long issueId, IssueDto issueDto, Long userId);
    void deleteIssue(Long issueId, Long userId);
    //List<Issue> getIssueByAssigneeId(Long assigneeId);
    //List<Issue> searchIssue(String title, String status, String priority, Long assigneeId);
    //List<User> getAssigneeForIssue(Long issueId);
    IssueDto addUserToIssue(Long issueId, Long userId);
    IssueDto updateIssueStatus(Long issueId, IssueStatusRequest status);

}
