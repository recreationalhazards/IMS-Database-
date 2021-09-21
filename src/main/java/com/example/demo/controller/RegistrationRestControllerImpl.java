package com.example.demo.controller;

import com.example.demo.persistence.model.dto.PasswordDto;
import com.example.demo.persistence.model.dto.UserDto;
import com.example.demo.error.InvalidOldPasswordException;
import com.example.demo.error.UserAlreadyExistException;
import com.example.demo.persistence.model.User;
import com.example.demo.persistence.model.VerificationToken;
import com.example.demo.security.ISecurityUserService;
import com.example.demo.service.IUserService;
import com.example.demo.util.GenericResponse;
import com.example.demo.util.RegistrationUtil;
import com.example.demo.util.UserServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@Component
public class RegistrationRestControllerImpl implements RegistrationRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;

    private ISecurityUserService securityUserService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Environment env;
    @Autowired
    RegistrationUtil registrationUtil;
    @Autowired
    UserServiceUtil userServiceUtil;

    public RegistrationRestControllerImpl() {
        super();
    }

    // Registration
    @PostMapping("/user/registration/verification")
    public GenericResponse registerUserAccount(@RequestBody @Valid final UserDto accountDto, final HttpServletRequest request) throws UserAlreadyExistException {
        LOGGER.debug("Registering user account with information: {}", accountDto);

        try {
            final User registered = userService.registerNewUserAccount(accountDto);
            final String token = UUID.randomUUID().toString();
            mailSender.send(registrationUtil.sendAccountActivationEmail(token, registered.getEmail(), registered));
            return new GenericResponse("success");
        } catch ( UserAlreadyExistException userAlreadyExistException ) {
            return new GenericResponse(messages.getMessage("message.regError", null, request.getLocale()));
        }
        // Will work on this later on
        // userService.addUserLocation(registered, getClientIP(request));
        // eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), (String) getAppUrl(request)));
    }

    @GetMapping("/user/registration/activation")
    public GenericResponse activateUserAccount(@RequestParam final String token, final HttpServletRequest request) {
        final VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken.getExpiryDate().before(Date.from(Instant.now()))) {
            return new GenericResponse(messages.getMessage("message.expired", null, request.getLocale()));
        } else {
            final User user = userService.getUser(verificationToken.getToken());
            userService.activateAccount(user);
            return new GenericResponse("success");
        }
    }

    // User activation - verification
    @GetMapping("/user/resendRegistrationToken")
    public GenericResponse sendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        mailSender.send(registrationUtil.constructResendVerificationTokenEmail((registrationUtil.getAppUrl(request).toString()), request.getLocale(), newToken, user));
        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

    // Send link to email after forgetting password to reset password. The link will redirect user to /user/savePassword
//    @PostMapping("/user/registration/sendOneTimePasswordLink")
//    public GenericResponse recoverPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
//        final User user = userService.findUserByEmail(userEmail);
//        if (user != null) {
//            final String token = UUID.randomUUID().toString();
//            userService.createPasswordResetTokenForUser(user, token);
//            mailSender.send(constructResetTokenEmail(getAppUrl(request).toString(), request.getLocale(), token, user));
//        }
//        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
//    }


    @PostMapping("/user/registration/oneTimePassword")
    public GenericResponse recoverPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {

        final User user = userService.findUserByEmail(userEmail);

        if (user != null) {
            final String tokenString = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, tokenString);
            final String oneTimePassword = userServiceUtil.generateOneTimePassword();
            userService.changeUserPassword(user, oneTimePassword, (60 * 24));
            SimpleMailMessage mailMessage = registrationUtil.constructEmail("Your one time password!", "Your one time password is: " + oneTimePassword + "\n\nThank You!\nChrist Covenant Church\n(314) 839-0292", user);
            mailSender.send(mailMessage);
        }

        return new GenericResponse("Your one time password has been sent to your email!");
    }

    // Reset password after forgetting old password
    @PostMapping("/user/resetPassword")
    public GenericResponse resetPassword(final Locale locale, @RequestBody @Valid PasswordDto passwordDto) {

        final String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());

        if (result != null) {
            return new GenericResponse(messages.getMessage("auth.message." + result, null, locale));
        }

        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
        if (user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDto.getNewPassword(), (365 * 60 * 24));
            return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
        } else {
            return new GenericResponse(messages.getMessage("auth.message.invalid", null, locale));
        }
    }

    // Change User password from old password
    @PostMapping("/user/updatePassword")
    public GenericResponse updatePassword(@RequestBody final Locale locale, @RequestBody @Valid PasswordDto passwordDto) {
        final User user = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword(), (60 * 24));
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }

    // Change user 2 factor authentication
    @PostMapping("/user/update/2fa")
    public GenericResponse modifyUser2FA(@RequestParam("use2FA") final boolean use2FA) throws UnsupportedEncodingException {
        final User user = userService.updateUser2FA(use2FA);
        if (use2FA) {
            return new GenericResponse(userService.generateQRUrl(user));
        }
        return null;
    }

    // Inactivate account
    @GetMapping("/user/inactivateAccount")
    public GenericResponse inactivateAccount(@RequestParam final String verificationToken) {
        final User user = userService.getUser(verificationToken);
        if (userService.deactivateAccount(verificationToken))
            return new GenericResponse("success");
        else
            return new GenericResponse("Error!", "account deactivation did not work");
    }

    // IMS-10 Validate and inactivate user
    @PostMapping("/user/deactivateAccount")
    public GenericResponse deactivateLostAccount(@RequestParam final String email) {
        final User user = userService.findUserByEmail(email);
        final String token = UUID.randomUUID().toString();
        mailSender.send(registrationUtil.sendAccountDeActivationEmail(token, user.getEmail(), user));
        return new GenericResponse("success");
    }

    @GetMapping("user/activateAccount")
    public GenericResponse activateAccount(@RequestParam final String email) {
        final User registered = userService.findUserByEmail(email);
        final String token = UUID.randomUUID().toString();
        mailSender.send(registrationUtil.sendAccountActivationEmail(token, registered.getEmail(), registered));
        return new GenericResponse("success");
    }
}
