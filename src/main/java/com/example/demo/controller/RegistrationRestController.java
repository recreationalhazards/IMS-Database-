package com.example.demo.controller;

import com.example.demo.error.InvalidOldPasswordException;
import com.example.demo.error.UserAlreadyExistException;
import com.example.demo.persistence.model.User;
import com.example.demo.persistence.model.VerificationToken;
import com.example.demo.persistence.model.dto.PasswordDto;
import com.example.demo.persistence.model.dto.UserDto;
import com.example.demo.security.ISecurityUserService;
import com.example.demo.service.IUserService;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.util.GenericResponse;
import com.example.demo.util.RegistrationUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("user")
public class RegistrationRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageSource messages;
    @Autowired

    private IUserService userService;
    @Autowired

    private JavaMailSender mailSender;
    @Autowired

    private RegistrationUtil registrationUtil;

    private ISecurityUserService securityUserService;
    @Autowired

    private UserServiceImpl userServiceImpl;

    // Registration
    @PostMapping("/registration/verification")
    public Response registerUserAccount(@RequestBody @Valid final UserDto accountDto, final HttpServletRequest request) throws UserAlreadyExistException {
        LOGGER.debug("Registering user account with information: {}", accountDto);

        try {
            final User registered = userService.registerNewUserAccount(accountDto);
            final String token = UUID.randomUUID().toString();
            try {
                mailSender.send(registrationUtil.sendAccountActivationEmail(token, registered.getEmail(), registered));
                return Response.status(Response.Status.OK).entity(registered).encoding(messages.getMessage("message.regSucc", null, null)).build();
            }  catch (MailSendException mailSendException) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).encoding(messages.getMessage("message.regUnSuccLink", null, null) + "\nReason" + mailSendException.getMessage().toString()).build();
            }
        } catch (UserAlreadyExistException userAlreadyExistException) {
            return Response.status(Response.Status.CONFLICT).encoding(messages.getMessage("message.regError", null, null)).build();
        }
    }

    @GetMapping("/registration/activation")
    public Response activateUserAccount(@RequestParam final String token, final HttpServletRequest request) {
        final VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken.getExpiryDate().before(Date.from(Instant.now()))) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).encoding(messages.getMessage("message.expired", null, null)).build();
        } else {
            final User user = userService.getUser(verificationToken.getToken());
            userService.activateAccount(user);
            return Response.status(Response.Status.OK).encoding(messages.getMessage("message.regSuccConfirmed", null, null)).build();
        }
    }

    // User activation - verification
    @GetMapping("/resendRegistrationToken")
    public Response sendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        try {
            mailSender.send(registrationUtil.constructResendVerificationTokenEmail((registrationUtil.getAppUrl(request).toString()), request.getLocale(), newToken, user));
            return Response.status(Response.Status.OK).encoding(messages.getMessage("message.resendToken", null, request.getLocale())).build();
        } catch (MailSendException mailSendException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).encoding(messages.getMessage("label.form.resendRegistrationToken", null, request.getLocale()) + "\nReason" + mailSendException.getMessage().toString()).build();
        }
    }

    @PostMapping("/registration/oneTimePassword")
    public Response recoverPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {

        final User user = userService.findUserByEmail(userEmail);

        if (user != null) {
            final String tokenString = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, tokenString);
            final String oneTimePassword = userServiceImpl.generateOneTimePassword();
            userService.changeUserPassword(user, oneTimePassword, (60 * 24));
            SimpleMailMessage mailMessage = registrationUtil.constructEmail("Your one time password!", "Your one time password is: " + oneTimePassword + "\n\nThank You!\nChrist Covenant Church\n(314) 839-0292", user);
            mailSender.send(mailMessage);
        }

        return Response.status(Response.Status.CREATED).encoding(messages.getMessage("Your one time password has been sent to your email!", null, request.getLocale())).build();
    }

    // Reset password after forgetting old password
    @PostMapping("/resetPassword")
    public Response resetPassword(final Locale locale, @RequestBody @Valid PasswordDto passwordDto) {

        final String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());

        if (result != null) {
            return Response.status(Response.Status.OK).encoding(messages.getMessage("auth.message." + result, null, locale)).build();
        }

        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
        if (user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDto.getNewPassword(), (365 * 60 * 24));
            return Response.status(Response.Status.OK).encoding(messages.getMessage("message.resetPasswordSuc", null, locale)).build();
        } else {
            return Response.status(Response.Status.CONFLICT).encoding(messages.getMessage("auth.message.invalid", null, locale)).build();
        }
    }

    // Change User password from old password
    @PostMapping("/updatePassword")
    public Response updatePassword(@RequestBody final Locale locale, @RequestBody @Valid PasswordDto passwordDto) {
        final User user = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword(), (60 * 24));
        return Response.status(Response.Status.NO_CONTENT).encoding(messages.getMessage("message.updatePasswordSuc", null, locale)).build();
    }

    // Change user 2 factor authentication
    @PostMapping("/update/2fa")
    public Response modifyUser2FA(@RequestParam("use2FA") final boolean use2FA) throws UnsupportedEncodingException {
        final User user = userService.updateUser2FA(use2FA);
        if (use2FA) {
            new GenericResponse(userService.generateQRUrl(user));
            return Response.status(200).encoding("Account authenticated successfully").build();
        }
        return Response.status(500).encoding("Error authenticating account").build();
    }

    // Inactivate account
    @GetMapping("/inactivateAccount")
    public Response inactivateAccount(@RequestParam final String verificationToken) {
        final User user = userService.getUser(verificationToken);
        if (userService.deactivateAccount(verificationToken))
            return Response.status(200).encoding(messages.getMessage("message.succInactivateAccount", null, null)).build();
        else
            return Response.status(420).encoding(messages.getMessage("message.unSuccInactivateAccount", null, null)).build();
    }

    // IMS-10 Validate and inactivate user
    @PostMapping("/deactivateAccount")
    public Response deactivateLostAccount(@RequestParam final String email) {
        final User user = userService.findUserByEmail(email);
        final String token = UUID.randomUUID().toString();
        try {
            mailSender.send(registrationUtil.sendAccountDeActivationEmail(token, user.getEmail(), user));
            return Response.status(200).encoding(messages.getMessage("message.deactivatingEmailSent", null, null)).build();
        } catch (MailSendException mailSendException) {
            return Response.status(424).encoding(messages.getMessage("message.deactivatingEmailUnsuccSent", null, null) + "\nReason: " + mailSendException.getMessage()).build();
        }
    }

    @GetMapping("/activateAccount")
    public Response activateAccount(@RequestParam final String email) {
        final User registered = userService.findUserByEmail(email);
        final String token = UUID.randomUUID().toString();
        try {
            mailSender.send(registrationUtil.sendAccountActivationEmail(token, registered.getEmail(), registered));
            return Response.status(200).encoding(messages.getMessage("message.activatingEmailSent", null, null)).build();
        } catch (MailSendException mailSendException) {
            return Response.status(424).encoding(messages.getMessage("message.activatingEmailUnsuccSent", null, null) + "\nReason: " + mailSendException.getMessage()).build();
        }
    }
}
