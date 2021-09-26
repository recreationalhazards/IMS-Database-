package com.example.demo.service;

import com.example.demo.error.UserAlreadyExistException;
import com.example.demo.persistence.dao.*;
import com.example.demo.persistence.model.*;
import com.example.demo.persistence.model.dto.UserDto;
import com.example.demo.util.UserServiceUtil;
import com.maxmind.geoip2.DatabaseReader;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Component
@Slf4j
@NoArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {

    private static final String TOKEN_VALID = "valid";
    private static final String TOKEN_EXPIRED = "expired";
    public static String APP_NAME = "SpringRegistration";
    private static final String TOKEN_INVALID = "invalidToken";
    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    @Autowired
    private Environment env;
    private UserRepository userRepository;
    @Qualifier("GeoIPCountry")
    private DatabaseReader databaseReader;
    private RoleRepository roleRepository;
    private SessionRegistry sessionRegistry;
    private VerificationTokenRepository tokenRepository;
    private UserLocationRepository userLocationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder = passwordEncoder();
    private PasswordResetTokenRepository passwordTokenRepository;
    private NewLocationTokenRepository newLocationTokenRepository;
    private UserServiceUtil userServiceUtil;

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Registration
    @Override
    public User registerNewUserAccount(final UserDto accountDto) throws UserAlreadyExistException {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + accountDto.getEmail());
        }

        final User user = userServiceUtil.userDtoToUser(accountDto);
        return userRepository.save(user);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    //Login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword().getNewPassword(), new ArrayList<>());
    }

    //GET USER
    @Override
    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    //DELETE USER
    @Override
    public void deleteUser(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }
        userRepository.delete(user);
    }


    //TOKENIZATION
    @Override
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID().toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    //No uses of these methods
    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    //Password
    @Override
    public void changeUserPassword(final User user, final String password, int expiryMinutes) {
        Password passwordEntity = user.getPassword();

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryMinutes);

        String salt = userServiceUtil.generateSalt();
        passwordEntity.setOldPassword(passwordEntity.getNewPassword());
        user.setSecret(salt);
        passwordEntity.setNewPassword(passwordEncoder.encode(salt + password));
        passwordEntity.setExpiryDate(new Date(cal.getTime().getTime()));
        user.setPassword(passwordEntity);
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword().getNewPassword());
    }

    //Activation
    @Override
    public void activateAccount(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public boolean deactivateAccount(String verificationToken) {
        final User user = getUser(verificationToken);
        user.setEnabled(false);
        userRepository.save(user);

        if (user.isEnabled())
            return false;
        else
            return true;
    }

    //2FA
    @Override
    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }

    @Override
    public User updateUser2FA(boolean use2FA) {
        final Authentication curAuth = SecurityContextHolder.getContext()
                .getAuthentication();
        User currentUser = (User) curAuth.getPrincipal();
        currentUser.setUsing2FA(use2FA);
        currentUser = userRepository.save(currentUser);
        final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), curAuth.getAuthorities());
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
        return currentUser;
    }

    @Override
    public List<String> getUsersFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals()
                .stream()
                .filter((u) -> !sessionRegistry.getAllSessions(u, false)
                        .isEmpty())
                .map(o -> {
                    if (o instanceof User) {
                        return (((User) o).getEmail());
                    } else {
                        return o.toString();
                    }
                }).collect(Collectors.toList());
    }


    //LOCATION: there are no uses of these methods as of yet
    @Override
    public NewLocationToken isNewLoginLocation(String username, String ip) {

        if (!isGeoIpLibEnabled()) {
            return null;
        }

        try {
            final InetAddress ipAddress = InetAddress.getByName(ip);
            final String country = databaseReader.country(ipAddress)
                    .getCountry()
                    .getName();
            System.out.println(country + "====****");
            final User user = userRepository.findByEmail(username);
            final UserLocation loc = userLocationRepository.findByCountryAndUser(country, user);
            if ((loc == null) || !loc.isEnabled()) {
                return createNewLocationToken(country, user);
            }
        } catch (final Exception e) {
            return null;
        }
        return null;
    }

    private NewLocationToken createNewLocationToken(String country, User user) {
        UserLocation loc = new UserLocation(country, user);
        loc = userLocationRepository.save(loc);

        final NewLocationToken token = new NewLocationToken(UUID.randomUUID()
                .toString(), loc);
        return newLocationTokenRepository.save(token);
    }

    private boolean isGeoIpLibEnabled() {
        return Boolean.parseBoolean(env.getProperty("geo.ip.lib.enabled"));
    }

    @Override
    public String isValidNewLocationToken(String token) {
        final NewLocationToken locToken = newLocationTokenRepository.findByToken(token);
        if (locToken == null) {
            return null;
        }
        UserLocation userLoc = locToken.getUserLocation();
        userLoc.setEnabled(true);
        userLoc = userLocationRepository.save(userLoc);
        newLocationTokenRepository.delete(locToken);
        return userLoc.getCountry();
    }

    @Override
    public void addUserLocation(User user, String ip) {

        if (!isGeoIpLibEnabled()) {
            return;
        }

        try {
            final InetAddress ipAddress = InetAddress.getByName(ip);
            final String country = databaseReader.country(ipAddress)
                    .getCountry()
                    .getName();
            UserLocation loc = new UserLocation(country, user);
            loc.setEnabled(true);
            userLocationRepository.save(loc);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
