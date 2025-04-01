package com.capstone.warehousesvc.dtos;


import com.capstone.warehousesvc.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User registration request")
public class RegisterRequest {

    @Schema(description = "User's full name", example = "John Smith", required = true)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "User's email address", example = "john.smith@example.com", required = true)
    @NotBlank(message = "email is required")
    private String email;

    @Schema(description = "User's password", example = "securePassword123", required = true)
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "User's phone number", example = "+1 234-567-8901", required = true)
    @NotBlank(message = "phoneNumber is required")
    private String phoneNumber;

    @Schema(description = "User's role in the system", example = "USER", defaultValue = "USER")
    private UserRole role;
}
