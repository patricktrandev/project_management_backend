package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.dto.*;
import com.blackcoffee.projectmanagement.entity.Chat;
import com.blackcoffee.projectmanagement.entity.Project;
import com.blackcoffee.projectmanagement.entity.User;
import com.blackcoffee.projectmanagement.exception.ProjectAPIException;
import com.blackcoffee.projectmanagement.exception.ResourceNotFoundException;
import com.blackcoffee.projectmanagement.repository.ChatRepository;
import com.blackcoffee.projectmanagement.repository.ProjectQueryRepository;
import com.blackcoffee.projectmanagement.repository.ProjectRepository;
import com.blackcoffee.projectmanagement.repository.UserRepository;
import com.blackcoffee.projectmanagement.service.ProjectService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ChatRepository chatRepository;
    private ModelMapper mapper;
    private ProjectQueryRepository projectQueryRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository,ChatRepository chatRepository, ModelMapper mapper,ProjectQueryRepository projectQueryRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.chatRepository=chatRepository;
        this.projectQueryRepository=projectQueryRepository;
        this.mapper = mapper;
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto,AuthResponse userDto) {
        User user= userRepository.findByEmail(userDto.getEmail());
        if(user==null){
            throw new ProjectAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found with email");
        }
        Project createdProject= new Project();
        createdProject.setOwner(user);
        StringBuilder stringBuilder= new StringBuilder();
        for(String tag: projectDto.getTags()){
            stringBuilder.append(tag);
            stringBuilder.append(",");
        }
        createdProject.setTags(String.valueOf(stringBuilder));
        createdProject.setName(projectDto.getName());
        createdProject.setCategory(projectDto.getCategory());
        createdProject.setDescription(projectDto.getDescription());
        createdProject.getTeam().add(user);
        Project savedProject= projectRepository.save(createdProject);
        Chat chat= new Chat();
        chat.setProject(createdProject);
        chat.setName(projectDto.getName());
        Chat projectChat= chatRepository.save(chat);

//        createdProject.setChat(projectChat);
//        Project savedProject= projectRepository.save(createdProject);
        ProjectDto projectCreated= mapToDtoWithChat(savedProject, projectChat);
        return projectCreated;
    }



    @Override
    public ProjectDto getProjectById(Long projectId) {
        Project project= projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project","id",projectId));
        List<UserByProject> userByProjects =projectRepository.findAllEmployeeByProject(projectId);

        return mapToDtoWithTeam(project, userByProjects);
    }

    @Override
    public ProjectDto updateProject(ProjectDto updatedProject, Long id,AuthResponse loggedInUser) {
        Project project= projectRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Project","id",id));
        User foundUser=userRepository.findByEmail(loggedInUser.getEmail());
        if(foundUser.getUserId()!=project.getOwner().getUserId()){
            throw new ProjectAPIException(HttpStatus.BAD_REQUEST,"Permission is denied");
        }
        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        StringBuilder stringBuilder= new StringBuilder();
        for(String tag: updatedProject.getTags()){
            stringBuilder.append(tag);
            stringBuilder.append(",");
        }
        project.setTags(String.valueOf(stringBuilder));
        Project updated=projectRepository.save(project);
        return mapToDto(updated);
    }

    @Override
    public void addUserToProject(Long projectId,String email) {
        Project project= projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project","id",projectId));
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new ProjectAPIException(HttpStatus.BAD_REQUEST,"User does not exist");
        }
        boolean check=false;
        List<UserByProject> userByProjects= projectRepository.findAllEmployeeByProject(project.getProjectId());
        for (UserByProject emp: userByProjects){
            if(emp.getUser_Id()==user.getUserId()){
                check=true;
                break;
            }
        }
        //System.out.println(check);
        if(check){
            throw new ProjectAPIException(HttpStatus.BAD_REQUEST,"User already joined  project");
        }


        project.getTeam().add(user);

        projectRepository.save(project);
    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) {
        Project project= projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project","id",projectId));
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id",userId));
        if(project.getTeam().contains(user)){
            project.getTeam().remove(user);
            //project.getChat().getUsers().remove(user);
        }

        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId, AuthResponse user) {
        Project project= projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project","id",projectId));
        User foundUser=userRepository.findByEmail(user.getEmail());
        if(foundUser.getUserId()!=project.getOwner().getUserId()){
            throw new ProjectAPIException(HttpStatus.BAD_REQUEST,"Permission is denied");
        }
        projectRepository.delete(project);

        /* dont forget delete chat entity  */

        //chatRepository.delete();

    }

    @Override
    public List<AllProjectsByUser> getAllProjectsParticipatedByUserId(Long userId) {
        List<AllProjectsByUser> allProjectsByUserList= projectRepository.findAllProjectsByUserId(userId);
        return allProjectsByUserList;
    }

    @Override
    public List<ProjectDto> searchProjectsByCondition(Map<String, String> params) {
       List<Project> projects= projectQueryRepository.getProjectsUsingWhereClause(params);
        List<ProjectDto> projectDtoList=new ArrayList<>();
        for(Project p: projects){
            projectDtoList.add(mapToDto(p));
        }
        return projectDtoList;
    }


    private ProjectDto mapToDtoWithChat(Project project, Chat chat) {
        ProjectDto projectDto= new ProjectDto();
        projectDto.setProjectId(project.getProjectId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setCategory(project.getCategory());
        projectDto.setTags(Collections.singletonList(project.getTags()));
        AuthResponse authResponse= new AuthResponse();
        authResponse.setId(project.getOwner().getUserId());
        authResponse.setEmail(project.getOwner().getEmail());
        authResponse.setFullName(project.getOwner().getFullName());
        projectDto.setOwner(authResponse);
        ChatDto chatDto= new ChatDto();
        chatDto.setProjectId(chat.getProject().getProjectId());
        chatDto.setChatId(chat.getId());
        chatDto.setName(chat.getName());


        return projectDto;
    }
    private ProjectDto mapToDto(Project project) {
        ProjectDto projectDto= new ProjectDto();
        projectDto.setProjectId(project.getProjectId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setTags(Collections.singletonList(project.getTags()));
        projectDto.setCategory(project.getCategory());
        AuthResponse authResponse= new AuthResponse();
        authResponse.setId(project.getOwner().getUserId());
        authResponse.setEmail(project.getOwner().getEmail());
        authResponse.setFullName(project.getOwner().getFullName());
        projectDto.setOwner(authResponse);
        ChatDto chatDto= new ChatDto();
        chatDto.setProjectId(project.getChat().getProject().getProjectId());
        chatDto.setChatId(project.getChat().getId());
        chatDto.setName(project.getChat().getName());
        projectDto.setChat(chatDto);

        return projectDto;
    }
    private ProjectDto mapToDtoWithTeam(Project project, List<UserByProject> userByProjectList) {
        ProjectDto projectDto= new ProjectDto();
        projectDto.setProjectId(project.getProjectId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setTags(Collections.singletonList(project.getTags()));
        projectDto.setCategory(project.getCategory());
        AuthResponse authResponse= new AuthResponse();
        authResponse.setId(project.getOwner().getUserId());
        authResponse.setEmail(project.getOwner().getEmail());
        authResponse.setFullName(project.getOwner().getFullName());
        projectDto.setOwner(authResponse);
        ChatDto chatDto= new ChatDto();
        chatDto.setProjectId(project.getChat().getProject().getProjectId());
        chatDto.setChatId(project.getChat().getId());
        chatDto.setName(project.getChat().getName());
        projectDto.setChat(chatDto);
        projectDto.setTeams(userByProjectList);
        return projectDto;
    }
}
