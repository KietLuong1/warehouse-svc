package com.capstone.warehousesvc.controllers;

import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.UserDTO;
import com.capstone.warehousesvc.models.User;
import com.capstone.warehousesvc.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "User account management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users", description = "List all users (Admin only)")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Update user", description = "Update user profile information")
    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @Operation(summary = "Delete user", description = "Delete a user account (Admin only)")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @Operation(summary = "Get user transactions", description = "Get all transactions for a specific user")
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<Response> getUserAndTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserTransactions(userId));
    }

    @Operation(summary = "Get current user", description = "Get details of currently logged-in user")
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentLoggedInUser());
    }
}
