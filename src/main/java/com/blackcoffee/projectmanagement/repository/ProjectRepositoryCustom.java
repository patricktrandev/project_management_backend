package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.entity.Project;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

public interface ProjectRepositoryCustom {
    List<Project> getProjectsUsingWhereClause(Map<String,String> params);
}
