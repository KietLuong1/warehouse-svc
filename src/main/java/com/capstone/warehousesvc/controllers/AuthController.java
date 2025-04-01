package com.capstone.warehousesvc.controllers;

import com.capstone.warehousesvc.dtos.LoginRequest;
import com.capstone.warehousesvc.dtos.RegisterRequest;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register new user", description = "Create new user account with role-based access")
    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

}
