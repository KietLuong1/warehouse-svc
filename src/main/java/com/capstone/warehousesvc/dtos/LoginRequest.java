package com.capstone.warehousesvc.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User login request")
public class LoginRequest {

    @Schema(description = "User's email address", example = "john.smith@example.com", required = true)
    @NotBlank(message = "email is required")
    private String email;

    @Schema(description = "User's password", example = "securePassword123", required = true)
    @NotBlank(message = "Password is required")
    private String password;
}
