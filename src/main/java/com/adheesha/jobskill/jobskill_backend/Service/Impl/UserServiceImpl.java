package com.adheesha.jobskill.jobskill_backend.Service.Impl;

import com.adheesha.jobskill.jobskill_backend.Service.UserService;
import com.adheesha.jobskill.jobskill_backend.dto.LoginRequestDto;
import com.adheesha.jobskill.jobskill_backend.dto.LoginResponseDto;
import com.adheesha.jobskill.jobskill_backend.dto.UserDto;
import com.adheesha.jobskill.jobskill_backend.dto.UserDtoReturn;
import com.adheesha.jobskill.jobskill_backend.entity.User;
import com.adheesha.jobskill.jobskill_backend.enums.Role;
import com.adheesha.jobskill.jobskill_backend.repo.UserRepo;
import com.adheesha.jobskill.jobskill_backend.utill.JWTTokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepo userRepo, JWTTokenGenerator jwtTokenGenerator, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDtoReturn registerUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(Base64.getEncoder().encodeToString(userDto.getPassword().getBytes()));

        User saved = userRepo.save(user);
        if (saved != null) {
            return new UserDtoReturn(saved.getEmail(), "Registered Successfully");
        }
        return new UserDtoReturn(userDto.getEmail(), "Registration failed");
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        Optional<User> userByEmail = userRepo.getUsersByEmail(loginRequestDto.getEmail());

        if (userByEmail.isPresent()) {
            User user = userByEmail.get();

            byte[] decodedBytes = Base64.getDecoder().decode(user.getPassword());
            String decodedPassword = new String(decodedBytes);

            if (decodedPassword.equals(loginRequestDto.getPassword())) {
                String token = jwtTokenGenerator.generateToken(user);

                return new LoginResponseDto(
                        user.getEmail(),
                        token,
                        user.getRole(),
                        user.getUserName() // Return full name
                );
            }

        }
        return new LoginResponseDto("Invalid credentials", null, null, null);
    }

    @Override
    public UserDto upgradeUserRole(Integer userId, String newRole) {
        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                user.setRole(Role.valueOf(newRole.toUpperCase()));
                User updatedUser = userRepo.save(user);
                return modelMapper.map(updatedUser, UserDto.class);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role provided: " + newRole);
            }
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
}
