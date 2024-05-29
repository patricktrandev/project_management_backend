package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.dto.AllProjectsByUser;
import com.blackcoffee.projectmanagement.dto.UserByProject;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectRepositoryTests {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectQueryRepository projectQueryRepository;

    @DisplayName("Unit test for find all employee joined in project")
    @Test
    public void givenProjectId_whenFindAllEmployeeByProject_thenGetAllEmployees(){
        Long projectId= 1L;
        List<UserByProject> employees= projectRepository.findAllEmployeeByProject(projectId);

        Assertions.assertThat(employees).isNotNull();

    }

    @DisplayName("Unit test for find all projects by user id")
    @Test
    public void givenUserId_whenFindAllProjectsByUserId_thenGetAllprojects(){
        Long userId=1L;
        List<AllProjectsByUser> projectList= projectRepository.findAllProjectsByUserId(userId);

        Assertions.assertThat(projectList).isNotNull();

    }
//    @DisplayName("Unit test for search project by custom condition")
//    @Test
//    public void givenKeySearch_whenGetProjectsUsingWhereClause_thenGetProjectObject(){
//        Map<String,String> params=new HashMap<>();
//        params.put("category","javascript");
//        params.put("tags","spring boot");
//        //params.put("name","1");
//
//
//
//        List<Project> projects=projectQueryRepository.getProjectsUsingWhereClause(params);
//        Assertions.assertThat(projects).isNotNull();
//    }
}
