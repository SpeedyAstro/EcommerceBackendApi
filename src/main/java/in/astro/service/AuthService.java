package in.astro.service;

import in.astro.dto.LoginCredentials;
import in.astro.dto.JwtResponse;
import in.astro.jwt.JwtAuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager manager;

    @Autowired
    JwtAuthenticationHelper jwtHelper;

    @Autowired
    UserDetailsService userDetailsService;


    public JwtResponse login(LoginCredentials jwtRequest) {

        //authenticate with Authentication manager
        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        System.out.println("UserDetails: " + userDetails);
        String token = jwtHelper.generateToken(userDetails.getUsername());

        JwtResponse response = JwtResponse.builder().jwtToken(token).build();
        return response;
    }


    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authenticationToken);

        }catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }

}
