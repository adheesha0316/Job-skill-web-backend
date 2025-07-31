package com.adheesha.jobskill.jobskill_backend.service.Impl;

import com.adheesha.jobskill.jobskill_backend.dto.LoginRequestDto;
import com.adheesha.jobskill.jobskill_backend.dto.LoginResponseDto;
import com.adheesha.jobskill.jobskill_backend.dto.UserDto;
import com.adheesha.jobskill.jobskill_backend.dto.UserDtoReturn;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserDtoReturn registerUser(UserDto userDto) {
        return null;
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        return null;
    }

    @Override
    public UserDto upgradeUserRole(Integer userId, String newRole) {
        return null;
    }
}
