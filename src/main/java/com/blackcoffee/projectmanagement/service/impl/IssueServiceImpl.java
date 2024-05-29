package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.dto.IssueDto;
import com.blackcoffee.projectmanagement.dto.IssueStatusRequest;
import com.blackcoffee.projectmanagement.dto.UserByProject;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.entity.Issue;
import com.blackcoffee.projectmanagement.entity.Project;
import com.blackcoffee.projectmanagement.entity.User;
import com.blackcoffee.projectmanagement.exception.ProjectAPIException;
import com.blackcoffee.projectmanagement.exception.ResourceNotFoundException;
import com.blackcoffee.projectmanagement.repository.IssueRepository;
import com.blackcoffee.projectmanagement.repository.ProjectRepository;
import com.blackcoffee.projectmanagement.repository.UserRepository;
import com.blackcoffee.projectmanagement.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IssueServiceImpl implements IssueService {
    private IssueRepository issueRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    @Autowired
    public IssueServiceImpl(IssueRepository issueRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }




    @Override
    public IssueDto getIssueById(Long issueId) {
        Issue issue=issueRepository.findById(issueId).orElseThrow(()-> new ResourceNotFoundException("Issue","id",issueId));
        return mapToDto(issue);
    }

    @Override
    public List<IssueDto> getIssueByProjectId(Long projectId) {
        List<Issue> issueList= issueRepository.findByProjectId(projectId);
        List<IssueDto> issueDtoList= new ArrayList<>();
        for(Issue i: issueList){
            IssueDto issueDto= mapToDto(i);
            issueDtoList.add(issueDto);

        }
        return issueDtoList;
    }

    @Override
    public IssueDto createIssue(Long userId, IssueDto issueDto) {
        Project project= projectRepository.findById(issueDto.getProjectId()).orElseThrow(()-> new ResourceNotFoundException("Project","id", issueDto.getProjectId()));
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id",userId));

        Issue issue= new Issue();
        issue.setTitle(issueDto.getTitle());
        issue.setDescription(issueDto.getDescription());
        issue.setStatus(issueDto.getStatus());
        issue.setProjectId(issueDto.getProjectId());
        issue.setPriority(issueDto.getPriority());
        issue.setDueDate(issueDto.getDueDate());
        List<User> assignedUser= new ArrayList<>();
        assignedUser.add(user);
        issue.setAssignee(assignedUser);
        issue.setProject(project);
        Issue savedIssue= issueRepository.save(issue);
        return mapToDto(issue);

    }



    @Override
    public void deleteIssue(Long issueId, Long userId) {
        Issue issue=issueRepository.findById(issueId).orElseThrow(()-> new ResourceNotFoundException("Issue","id",issueId));

        issueRepository.delete(issue);
    }

    @Override
    public IssueDto addUserToIssue(Long issueId, Long userId) {
        Issue issue=issueRepository.findById(issueId).orElseThrow(()-> new ResourceNotFoundException("Issue","id",issueId));
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id",userId));
        Long checkIfUserAssignedToIssue= issueRepository.existIfUserIsAdded(user.getUserId(), issueId);
        if(checkIfUserAssignedToIssue>0){
            throw new ProjectAPIException(HttpStatus.BAD_REQUEST, "User already assigned to issue");
        }

        List<User> issueAssigneeList= issue.getAssignee();
        issueAssigneeList.add(user);
        issue.setAssignee(issueAssigneeList);
        Issue updatedIssue=issueRepository.save(issue);
        return mapToDto(updatedIssue);
    }

    @Override
    public IssueDto updateIssueStatus(Long issueId, IssueStatusRequest status) {
        Issue issue=issueRepository.findById(issueId).orElseThrow(()-> new ResourceNotFoundException("Issue","id",issueId));
        issue.setStatus(status.getStatus().trim());
        Issue updatedIssue= issueRepository.save(issue);
        return mapToDto(updatedIssue);
    }
    private IssueDto mapToDto(Issue issue) {
        IssueDto issueDto= new IssueDto();

        issueDto.setTitle(issue.getTitle());
        issueDto.setDescription(issue.getDescription());
        issueDto.setPriority(issue.getPriority());
        issueDto.setStatus(issue.getStatus());
        issueDto.setDueDate(issue.getDueDate());
        issueDto.setProjectId(issue.getProjectId());
        issueDto.setId(issue.getIssueId());
        //issueDto.setTag
        List<UserDto> assignedUser= new ArrayList<>();
        for(User foundIssue:issue.getAssignee()){
            UserDto userDto= new UserDto();
            userDto.setUserId(foundIssue.getUserId());
            userDto.setEmail(foundIssue.getEmail());
            userDto.setFullName(foundIssue.getFullName());
            userDto.setProjectSize(foundIssue.getProjectSize());
            assignedUser.add(userDto);
        }

        issueDto.setAssignee(assignedUser);
        return issueDto;
    }
}
