package com.example.demo.service;

import com.example.demo.error.UserAlreadyExistException;
import com.example.demo.persistence.model.NewLocationToken;
import com.example.demo.persistence.model.PasswordResetToken;
import com.example.demo.persistence.model.User;
import com.example.demo.persistence.model.VerificationToken;
import com.example.demo.persistence.model.dto.UserDto;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    void activateAccount(User user);
    User updateUser2FA(boolean use2FA);
    User findUserByEmail(String email);
    User getUser(String verificationToken);
    boolean deactivateAccount(String verificationToken);
    Optional<User> getUserByPasswordResetToken(String token);
    boolean checkIfValidOldPassword(User user, String password);
    VerificationToken generateNewVerificationToken(String token);
    void createPasswordResetTokenForUser(User user, String token);
    VerificationToken getVerificationToken(String VerificationToken);
    String generateQRUrl(User user) throws UnsupportedEncodingException;
    void changeUserPassword(User user, String password, int expiryMinutes);
    User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

    void deleteUser(User user);
    void saveRegisteredUser(User user);
    Optional<User> getUserByID(long id);
    void addUserLocation(User user, String ip);
    List<String> getUsersFromSessionRegistry();
    String isValidNewLocationToken(String token);
    String validateVerificationToken(String token);
    PasswordResetToken getPasswordResetToken(String token);
    NewLocationToken isNewLoginLocation(String username, String ip);
}
