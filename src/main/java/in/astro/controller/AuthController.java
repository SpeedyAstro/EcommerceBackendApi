package in.astro.controller;

import in.astro.dto.LoginCredentials;
import in.astro.dto.UserDto;
import in.astro.jwt.JwtAuthenticationHelper;
import in.astro.service.AuthService;
import in.astro.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Auth Controller", description = "[👮] Auth Controller")
@SecurityRequirement(name = "E-Commerce Application")
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtAuthenticationHelper jwtUtil;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public String test() {
        return "Hello from AuthController";
    }

    @PostMapping("/register")
    @Operation(summary = "Register User", description = "[🎟️] Register User")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserDto user) {
        String encodedPass = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPass);
        UserDto userDTO = service.registerUser(user);
        String token = jwtUtil.generateToken(userDTO.getEmail());
//        return userDto and token both
        Map<String, Object> response = new HashMap<>();
        response.put("user", userDTO);
        response.put("jwt-token", token);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    @Operation(summary = "Login User", description = "[💂‍♂️] Login User")
    public Map<String ,Object> login(@RequestBody LoginCredentials credentials) {
        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                credentials.getEmail(), credentials.getPassword());

        authenticationManager.authenticate(authCredentials);

        String token = jwtUtil.generateToken(credentials.getEmail());

        return Collections.singletonMap("jwt-token", token);
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkAuthorized() {
        return ResponseEntity.ok("Authorized");
    }


}
