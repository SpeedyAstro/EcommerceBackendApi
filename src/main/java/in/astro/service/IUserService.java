package in.astro.service;

import in.astro.dto.UserDto;
import in.astro.dto.UserResponse;

public interface IUserService {
    UserDto registerUser(UserDto userDto);

    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserDto getUserById(Long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    String deleteUser(Long userId);
    UserDto findByUserEmail(String email);

    String changeRole(Long userId, String role);
}
