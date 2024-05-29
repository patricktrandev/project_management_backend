package com.blackcoffee.projectmanagement.service;

import com.blackcoffee.projectmanagement.dto.SubcriptionDto;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.entity.PlanType;
import com.blackcoffee.projectmanagement.entity.Subcription;

public interface SubcriptionService {
    Subcription createSubcription(UserDto user);
    SubcriptionDto getUserSubcription(Long userId);
    void upgradeSubcription(Long userId, PlanType planType);
    boolean isValid(Subcription subcription);
}
