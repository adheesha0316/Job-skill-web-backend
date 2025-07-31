package com.adheesha.jobskill.jobskill_backend.service.Impl;

import com.adheesha.jobskill.jobskill_backend.dto.LoginRequestDto;
import com.adheesha.jobskill.jobskill_backend.dto.LoginResponseDto;
import com.adheesha.jobskill.jobskill_backend.dto.UserDto;
import com.adheesha.jobskill.jobskill_backend.dto.UserDtoReturn;

public interface UserService {
    UserDtoReturn registerUser(UserDto userDto);
    LoginResponseDto loginUser(LoginRequestDto loginRequestDto);
    UserDto upgradeUserRole(Integer userId, String newRole);
}
