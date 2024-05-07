package in.astro.controller;

import in.astro.config.AppConstants;
import in.astro.dto.UserDto;
import in.astro.dto.UserResponse;
import in.astro.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "User Controller", description = "[Get Users , Update User, Delete User, Get User By Id]")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    @Operation(summary = "Get Users", description = "[🔍] Get Users")
    public ResponseEntity<UserResponse> getUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        UserResponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.FOUND);
    }

    @GetMapping("/public/users/{userId}")
    @Operation(summary = "Get User By Id", description = "[🔍] Get User By Id")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        UserDto user = userService.getUserById(userId);
        return new ResponseEntity<UserDto>(user, HttpStatus.FOUND);
    }

    @PutMapping("/public/users/{userId}")
    @Operation(summary = "Update User", description = "[🔄] Update User")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDTO, @PathVariable Long userId) {
        UserDto updatedUser = userService.updateUser(userId, userDTO);
        return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{userId}")
    @Operation(summary = "Delete User", description = "[❌] Delete User")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        String status = userService.deleteUser(userId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

}
