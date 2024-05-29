package com.blackcoffee.projectmanagement.utils;

import java.util.Map;

public class BuildQuerySearch {
    public StringBuilder buildWhereClause(Map<String, String> params) {
        StringBuilder sqlQuery =new StringBuilder(" WHERE ");
        StringBuilder whereQuery = new StringBuilder();
        buildQueryCommon(params, whereQuery);
        sqlQuery.append(whereQuery);

        return sqlQuery;
    }
    public void buildQueryCommon(Map<String, String> params, StringBuilder whereQuery){
        String name = params.getOrDefault("name", null);
        if (name!=null && name!=""){
            whereQuery.append("\n name LIKE :name ");
        }
        String category = params.getOrDefault("category", null);
        if (name!=null &&category!=null && category!=""){
            whereQuery.append("\n AND category LIKE :category ");
        }else if(category!=null && category!=""){
            whereQuery.append("\n category LIKE :category ");
        }
        String tags = params.getOrDefault("tags", null);
        if ((name!=null || category!=null) && tags!=null && tags!=""){
            whereQuery.append("\n AND tags LIKE :tags");
        }else if(tags!=null && tags!=""){
            whereQuery.append("\n tags LIKE :tags");
        }
    }
}
