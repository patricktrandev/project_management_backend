package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.entity.Project;
import com.blackcoffee.projectmanagement.utils.BuildQuerySearch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProjectQueryRepository implements ProjectRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    public List<Project> getProjectsUsingWhereClause( Map<String,String> params){
        StringBuilder sqlQuery= new StringBuilder("select p \n" +
                "from projects p ");
        BuildQuerySearch where= new BuildQuerySearch();
        String name = params.getOrDefault("name", null);
        String category = params.getOrDefault("category", null);
        String tags = params.getOrDefault("tags", null);
        if(name!=null || category!=null || tags!=null){
            sqlQuery.append(where.buildWhereClause(params));
        }


        TypedQuery<Project> query = em.createQuery(sqlQuery.toString(), Project.class);

        if (name!=null && name!=""){
            query.setParameter("name","%"+name+"%");
        }

        if (category!=null ){
            query.setParameter("category","%"+category.toLowerCase()+"%");
        }

        if (tags!=null && tags!=""){
            query.setParameter("tags","%"+tags+"%");
        }
        return query.getResultList();
    }
}
