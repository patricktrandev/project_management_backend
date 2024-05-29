package com.blackcoffee.projectmanagement.dto;

public interface AllProjectsByUser {
    public String getEmail();
    public String getFullName();
    public String getTags();
    public String getCategory();
    public String getName();
    public Long getOwnerUserId();
    public Long getUserId();
}
