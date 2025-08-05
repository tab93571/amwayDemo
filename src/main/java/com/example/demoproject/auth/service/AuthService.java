package com.example.demoproject.auth.service;

import com.example.demoproject.auth.dto.LoginRequest;
import com.example.demoproject.auth.dto.LoginResponse;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.example.demoproject.security.JwtUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

@Service
public class AuthService implements UserDetailsService {
    
    private final AuthUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public AuthService(AuthUserRepository userRepository, @Lazy AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * Authenticate user and generate JWT token
     * @param loginRequest Login credentials
     * @return Login response with JWT token
     */
    public LoginResponse login(LoginRequest loginRequest) {
        try {
        
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            // Get user details and generate token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");
            
            // Get the actual User entity to access the ID
            com.example.demoproject.auth.entity.User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));
            
            String token = jwtUtil.generateToken(userDetails.getUsername(), role, user.getId());
            
            return new LoginResponse(token, userDetails.getUsername());
            
        } catch (Exception e) {
            return new LoginResponse("Invalid username or password");
        }
    }
    
    /**
     * Load user by username for Spring Security
     * @param username The username to load
     * @return UserDetails object
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
} 