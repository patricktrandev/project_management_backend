package com.blackcoffee.projectmanagement.dto;

import com.blackcoffee.projectmanagement.entity.Project;
import com.blackcoffee.projectmanagement.entity.User;

import java.time.LocalDate;
import java.util.Date;

public interface MessageByChat {
    public Long getId();
    public String getName();
    public int getProject_Project_Id();
    public String getContent();
    public Date getCreated_At();
    public int getSender_User_Id();
}
