package com.snapstore.identity_service.service;

import com.snapstore.identity_service.dto.request.SignupRequest;
import com.snapstore.identity_service.dto.response.SignupResponse;
import com.snapstore.identity_service.entity.User;
import com.snapstore.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signupNewUser(SignupRequest request) {

        if(userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exist !!");

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        User user = userRepository.save(newUser);
        System.out.println(
                user.getName() + " " +
                user.getEmail() + " " +
                user.getPassword()
        );
        SignupResponse response = new SignupResponse();
        response.setAccessToken("token");
        return response;
    }
}
