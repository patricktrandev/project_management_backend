package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectId(Long projectId);

    @Query(value = "select count(user_id) from issue_user where user_id=:id and issue_id=:issueId", nativeQuery = true)
    Long existIfUserIsAdded(@Param("id") Long id,@Param("issueId") Long issueId);


}
