package com.example.demo.controller;

import com.example.demo.error.UserAlreadyExistException;
import com.example.demo.persistence.model.dto.PasswordDto;
import com.example.demo.persistence.model.dto.UserDto;
import com.example.demo.util.GenericResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public interface RegistrationRestController {

    GenericResponse registerUserAccount(UserDto accountDto, HttpServletRequest request) throws UserAlreadyExistException;

    GenericResponse activateUserAccount(String token, HttpServletRequest request);

    GenericResponse sendRegistrationToken(HttpServletRequest request, final String existingToken);

    GenericResponse recoverPassword(HttpServletRequest request, String userEmail);

    GenericResponse resetPassword(Locale locale, PasswordDto passwordDto);

    GenericResponse updatePassword(Locale locale, PasswordDto passwordDto);

    GenericResponse modifyUser2FA(boolean use2FA) throws UnsupportedEncodingException;

    GenericResponse inactivateAccount(String verificationToken);

    GenericResponse deactivateLostAccount(String email);

    GenericResponse activateAccount(String email);
}

