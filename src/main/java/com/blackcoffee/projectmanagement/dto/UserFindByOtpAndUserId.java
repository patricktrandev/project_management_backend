package com.blackcoffee.projectmanagement.dto;

import java.time.LocalDateTime;

public interface UserFindByOtpAndUserId {
    public Long getId();
    public LocalDateTime getExpiration_Time();
    public Long getOtp();
    public Long getUser_User_Id();
}
