package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.LoginRequest;
import com.capstone.warehousesvc.dtos.RegisterRequest;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.UserDTO;
import com.capstone.warehousesvc.models.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);
}
