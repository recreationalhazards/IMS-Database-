package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.error.UserAlreadyExistException;
import com.example.demo.persistence.model.NewLocationToken;
import com.example.demo.persistence.model.PasswordResetToken;
import com.example.demo.persistence.model.User;
import com.example.demo.persistence.model.VerificationToken;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    VerificationToken createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    Optional<User> getUserByID(long id);

    void changeUserPassword(User user, String password, int expiryMinutes);

    boolean checkIfValidOldPassword(User user, String password);

    String validateVerificationToken(String token);

    String generateQRUrl(User user) throws UnsupportedEncodingException;

    User updateUser2FA(boolean use2FA);

    List<String> getUsersFromSessionRegistry();

    NewLocationToken isNewLoginLocation(String username, String ip);

    String isValidNewLocationToken(String token);

    void addUserLocation(User user, String ip);

    void activateAccount(User user);

    boolean deactivateAccount(String verificationToken);
 OneTimePassword

    String generateOneTimePassword();

 dev
}
