package com.simao.perfumehub.controllers;

import com.simao.perfumehub.docs.AdminControllerDocs;
import com.simao.perfumehub.dtos.user.ChangeUserRoleRequestDto;
import com.simao.perfumehub.dtos.user.UserRoleResponseDto;
import com.simao.perfumehub.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController implements AdminControllerDocs {

    private final UserDetailsServiceImpl userService;

    public AdminController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<UserRoleResponseDto> changeRoleUser(@PathVariable Long userId, @RequestBody @Valid ChangeUserRoleRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserRole(userId, dto));
    }
}
