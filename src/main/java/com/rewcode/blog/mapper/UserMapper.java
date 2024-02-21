package com.rewcode.blog.mapper;

import com.rewcode.blog.entity.Role;
import com.rewcode.blog.entity.User;
import com.rewcode.blog.payload.RegisterDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public User convertToUser(RegisterDto registerDto, Set<Role> roles){
        User user = modelMapper.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        return user;
    }

}
