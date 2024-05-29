package com.blackcoffee.projectmanagement.service.impl;

import com.blackcoffee.projectmanagement.dto.SubcriptionDto;
import com.blackcoffee.projectmanagement.dto.UserDto;
import com.blackcoffee.projectmanagement.entity.PlanType;
import com.blackcoffee.projectmanagement.entity.Subcription;
import com.blackcoffee.projectmanagement.entity.User;

import com.blackcoffee.projectmanagement.repository.SubcriptionRepository;
import com.blackcoffee.projectmanagement.repository.UserRepository;
import com.blackcoffee.projectmanagement.service.SubcriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class SubcriptionServiceImpl implements SubcriptionService {

    private SubcriptionRepository subcriptionRepository;
    private UserRepository userRepository;
    @Autowired
    public SubcriptionServiceImpl(SubcriptionRepository subcriptionRepository, UserRepository userRepository) {
        this.subcriptionRepository = subcriptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Subcription createSubcription(UserDto user) {

        Subcription subcription= new Subcription();
        subcription.setStartDate(LocalDate.now());
        subcription.setEndDate(LocalDate.now().plusMonths(12));
        subcription.setValid(true);
        User subUser= new User();
        subUser.setUserId(user.getUserId());
        subUser.setEmail(user.getEmail());
        subUser.setFullName(user.getFullName());
        subcription.setUser(subUser);
        subcription.setPlan(PlanType.FREE);
        return subcriptionRepository.save(subcription);
    }

    @Override
    public SubcriptionDto getUserSubcription(Long userId) {
        Subcription subcription = subcriptionRepository.findByUserUserId(userId);

        LocalDate currentDate= LocalDate.now();
        if(subcription.getPlan().equals(PlanType.FREE) && subcription.getEndDate().isBefore(currentDate)){
            subcription.setStartDate(LocalDate.now());
            subcription.setEndDate(LocalDate.now().plusMonths(12));
        }else if(!(subcription.getPlan().equals(PlanType.FREE)) && currentDate.isAfter(subcription.getEndDate())){
            subcription.setPlan(PlanType.FREE);
            subcription.setStartDate(LocalDate.now());
            subcription.setEndDate(LocalDate.now().plusMonths(12));
        }
        Subcription currentSub= subcriptionRepository.save(subcription);
        return mapToDto(currentSub);

    }



    @Override
    public void upgradeSubcription(Long userId, PlanType planType) {
        Subcription foundUser= subcriptionRepository.findByUserUserId(userId);


        int currentPlan= 0;
        if(planType.equals(PlanType.MONTHLY)){
            currentPlan= 1;
        }else if(planType.equals(PlanType.ANNUALLY)){
            currentPlan=2;
        }

        LocalDate currentDate= LocalDate.now();

          subcriptionRepository.updateSubcription(currentPlan, Math.toIntExact(userId), currentDate);


    }

    @Override
    public boolean isValid(Subcription subcription) {
        if(subcription.getPlan().equals(PlanType.FREE)){
            return true;
        }
        LocalDate endDate= subcription.getEndDate();
        LocalDate currentDate= LocalDate.now();

        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate);
    }
    private SubcriptionDto mapToDto(Subcription subcription) {
        SubcriptionDto subcriptionDto= new SubcriptionDto();
        subcriptionDto.setId(subcription.getId());
        subcriptionDto.setStartDate(subcription.getStartDate());
        subcriptionDto.setEndDate(subcription.getEndDate());
        subcriptionDto.setValid(subcription.isValid());
        subcriptionDto.setPlan(subcription.getPlan());

        UserDto userDto= new UserDto();
        userDto.setEmail(subcription.getUser().getEmail());
        userDto.setUserId(subcription.getUser().getUserId());
        userDto.setFullName(subcription.getUser().getFullName());
        subcriptionDto.setUser(userDto);
        return subcriptionDto;
    }
}
