package com.capstone.warehousesvc.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.capstone.warehousesvc.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "User account details")
public class UserDTO {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User's full name", example = "John Smith")
    private String name;

    @Schema(description = "User's email address", example = "john.smith@example.com")
    private String email;

    @Schema(description = "User's password (hidden in responses)", hidden = true)
    @JsonIgnore
    private String password;

    @Schema(description = "User's phone number", example = "+1 234-567-8901")
    private String phoneNumber;

    @Schema(description = "User's role in the system", example = "ADMIN")
    private UserRole role;

    @Schema(description = "User's transaction history")
    private List<TransactionDTO> transactions;

    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;
}
