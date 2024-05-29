package com.blackcoffee.projectmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String fullName;
    private String email;
    private String password;

    @JsonIgnore
    @ManyToMany(cascade ={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "issue_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "issue_id"))
    private List<Issue> assignedIssue;
    private int projectSize;

    @ManyToMany(cascade ={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "project_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projectList;

    @ManyToMany(cascade ={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "chat_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private List<Chat> chatList;
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ResetPassword resetPassword;

}
