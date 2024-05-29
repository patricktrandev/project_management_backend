package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.entity.Subcription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface SubcriptionRepository extends JpaRepository<Subcription, Long> {
    Subcription findByUserUserId(Long id);
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(nativeQuery = true, value = "update subcriptions \n" +
            "set plan=:plan, start_date=:startDate where user_user_id=:userId ")
    void updateSubcription(@Param("plan") int plan, @Param("userId") int userId, @Param("startDate")LocalDate date);

}
