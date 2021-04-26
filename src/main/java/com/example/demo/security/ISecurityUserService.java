package com.example.demo.security;


public interface ISecurityUserService {
    String validatePasswordResetToken(String token);
}
