package com.blackcoffee.projectmanagement.repository;

import com.blackcoffee.projectmanagement.dto.UserFindByOtpAndUserId;
import com.blackcoffee.projectmanagement.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    @Query(value = "select id, expiration_time, otp, user_user_id \n" +
            "from reset_password \n" +
            "where otp=:otp and user_user_id=:userId", nativeQuery = true)
    UserFindByOtpAndUserId findByOtpAndUser(@Param("otp") Long otp, @Param("userId")Long userId);

    ResetPassword findByUserUserId(Long id);
    @Transactional
    @Modifying
    @Query(value = "delete\n" +
            "from reset_password\n" +
            "where user_user_id=:id", nativeQuery = true)
    void deleteByUserUserId(@Param("id") Long id);
}
