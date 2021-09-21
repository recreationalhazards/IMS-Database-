package com.example.demo.util;

import com.example.demo.persistence.model.User;
import com.example.demo.persistence.model.VerificationToken;
import com.example.demo.security.ISecurityUserService;
import com.example.demo.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class RegistrationUtil {
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

    public SimpleMailMessage sendAccountActivationEmail(final String token, final String email, final User user) {
        final String subject = "Activate your account!";
        final String url = "http://localhost:8081/user/registration/activation?token=";
        final String body = url + userService.createVerificationTokenForUser(user, token).getToken();
        return constructEmail(subject, body, user);
    }

    public SimpleMailMessage sendAccountDeActivationEmail(final String token, final String email, final User user) {
        final String subject = "To deactivate your account, press the link below!";
        final String url = "http://localhost:8081/user/inactivateAccount?verificationToken=";
        final String body = url + userService.createVerificationTokenForUser(user, token).getToken();
        return constructEmail(subject, body, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    public SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/user/registration/activation?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    public SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setFrom(env.getProperty("spring.mail.username"));
        return simpleMailMessage;
    }

    public Object getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
