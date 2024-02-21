package com.rewcode.blog.service;

import com.rewcode.blog.payload.LoginDto;
import com.rewcode.blog.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
