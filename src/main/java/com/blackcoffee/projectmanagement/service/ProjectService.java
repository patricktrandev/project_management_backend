package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.dto.AllProjectsByUser;
import com.blackcoffee.projectmanagement.dto.AuthResponse;
import com.blackcoffee.projectmanagement.dto.ProjectDto;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.entity.Chat;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto, AuthResponse userDto);

    ProjectDto getProjectById(Long projectId);
    ProjectDto updateProject(ProjectDto updatedProject, Long id,AuthResponse loggedInUser);
    void addUserToProject(Long projectId,String email);
    void removeUserFromProject(Long projectId, Long userId);
    void deleteProject(Long projectId, AuthResponse user);



    List<AllProjectsByUser> getAllProjectsParticipatedByUserId(Long userId);

    List<ProjectDto> searchProjectsByCondition(Map<String, String> params);
}
