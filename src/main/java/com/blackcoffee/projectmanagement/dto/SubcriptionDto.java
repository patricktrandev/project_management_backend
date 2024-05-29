package com.blackcoffee.projectmanagement.dto;

import com.blackcoffee.projectmanagement.entity.PlanType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubcriptionDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private PlanType plan;
    private boolean isValid;
    private UserDto user;
}
