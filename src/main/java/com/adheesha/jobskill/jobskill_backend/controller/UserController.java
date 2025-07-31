package com.adheesha.jobskill.jobskill_backend.controller;

import com.adheesha.jobskill.jobskill_backend.Service.UserService;
import com.adheesha.jobskill.jobskill_backend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDtoReturn> userRegister(@RequestBody UserDto userDto) {
        UserDtoReturn userDtoReturn = userService.registerUser(userDto);
        return new ResponseEntity<>(userDtoReturn, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> userLogin(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginDto = userService.loginUser(loginRequestDto);
        return new ResponseEntity<>(loginDto, HttpStatus.OK);
    }

    @PutMapping("/upgrade-role/{userId}")
    public ResponseEntity<UserDto> upgradeUserRole(@PathVariable Integer userId,
                                                   @RequestBody RoleUpdateDto roleUpdateDto) {
        try {
            UserDto upgradedUser = userService.upgradeUserRole(userId, roleUpdateDto.getNewRole());
            return new ResponseEntity<>(upgradedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
