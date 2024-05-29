package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.dto.AllProjectsByUser;
import com.blackcoffee.projectmanagement.dto.UserByProject;
import com.blackcoffee.projectmanagement.entity.Project;
import com.blackcoffee.projectmanagement.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(nativeQuery = true, value="select u.email, u.full_name, p.tags, p.category, p.name, p.owner_user_id, pu.user_id \n" +
            "from project_user as pu \n" +
            "join users as u on pu.user_id = u.user_id \n" +
            "join projects as p on p.project_id= pu.project_id \n" +
            "where pu.project_id=:projectId")
    List<UserByProject>  findAllEmployeeByProject(@Param("projectId") Long projectId);



    @Query(nativeQuery = true, value = "select u.email, u.full_name, p.tags, p.category, p.name, p.owner_user_id, pu.user_id \n" +
        "from project_user as pu \n" +
        "join users as u on pu.user_id = u.user_id \n" +
        "join projects as p on p.project_id= pu.project_id \n" +
        "where pu.user_id=:id")
    List<AllProjectsByUser> findAllProjectsByUserId(@Param("id") Long id);



}
