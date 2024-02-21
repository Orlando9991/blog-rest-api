package com.rewcode.blog.service.impl;

import com.rewcode.blog.entity.Role;
import com.rewcode.blog.entity.User;
import com.rewcode.blog.exception.UserAlreadyExistsException;
import com.rewcode.blog.mapper.UserMapper;
import com.rewcode.blog.payload.LoginDto;
import com.rewcode.blog.payload.RegisterDto;
import com.rewcode.blog.repository.RoleRepository;
import com.rewcode.blog.repository.UserRepository;
import com.rewcode.blog.service.AuthService;
import com.rewcode.blog.utils.AppConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           RoleRepository roleRepository) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "User Logged-in successfully";
    }


    @Override
    public String register(RegisterDto registerDto) {
        //add for username exists in database

        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new UserAlreadyExistsException(registerDto.getUsername());
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new UserAlreadyExistsException(registerDto.getEmail());
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(AppConstants.ROLE_USER).get());

        User user = userMapper.convertToUser(registerDto, roles);
        userRepository.save(user);

        return "User registered successfully";
    }
}
