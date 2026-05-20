package com.snapstore.identity_service.service;

import com.snapstore.identity_service.dto.request.LoginRequest;
import com.snapstore.identity_service.dto.request.SignupRequest;
import com.snapstore.identity_service.dto.response.LoginResponse;
import com.snapstore.identity_service.dto.response.SignupResponse;
import com.snapstore.identity_service.entity.User;
import com.snapstore.identity_service.repository.UserRepository;
import com.snapstore.identity_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public SignupResponse signup(SignupRequest request) {

        if(userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exist !!");

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        User user = userRepository.save(newUser);

        String accessToken = jwtService.generateToken(user.getEmail());
        System.out.println(accessToken);
        return SignupResponse.builder().accessToken(accessToken).build();
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String accessToken = jwtService.generateToken(request.getEmail());

        return LoginResponse.builder().accessToken(accessToken).build();
    }
}
