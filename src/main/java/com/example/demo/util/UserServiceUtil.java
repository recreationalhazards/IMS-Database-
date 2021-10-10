package com.example.demo.util;

import com.example.demo.persistence.dao.PasswordRepository;
import com.example.demo.persistence.dao.RoleRepository;
import com.example.demo.persistence.model.Password;
import com.example.demo.persistence.model.Role;
import com.example.demo.persistence.model.User;
import com.example.demo.persistence.model.dto.PasswordDto;
import com.example.demo.persistence.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

public class UserServiceUtil {

    @Autowired
    private PasswordEncoder passwordEncoder = passwordEncoder();

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    public User userDtoToUser(final UserDto userDto) {
        final User user = new User();
        final PasswordDto passwordDto = new PasswordDto();
        String salt = generateSalt();
        passwordDto.setNewPassword(passwordEncoder.encode(userDto.getPassword()));
        passwordDto.setExpiryDate(365 * 24 * 60);
        Password password = passwordDtoToPasswordConversion(passwordDto);
        passwordRepository.save(password);

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(password);
        user.setEmail(userDto.getEmail());
        user.setUsing2FA(userDto.isUsing2FA());
        giveUserARole(userDto, user);
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setSecret(salt);

        return user;
    }

    private Password passwordDtoToPasswordConversion(PasswordDto passwordDto) {
        Password password = new Password();
        password.setNewPassword(passwordDto.getNewPassword());
        password.setOldPassword(passwordDto.getOldPassword());
        password.setExpiryDate(passwordDto.getExpiryDate());
        password.setToken(passwordDto.getToken());
        return password;
    }

    public String generateSalt() {
        String salt = generateOneTimePassword();
        return salt;
    }

    public String generateOneTimePassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digitalCharacter = "0123456789";
        String specialCharacter = "!@#$%&";

        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(13);
        for (int i = 0; i < 2; i++)
            sb.append(upperCase.charAt(rnd.nextInt(upperCase.length())));

        for (int i = 0; i < 7; i++)
            sb.append(lowerCase.charAt(rnd.nextInt(lowerCase.length())));

        for (int i = 0; i < 2; i++)
            sb.append(digitalCharacter.charAt(rnd.nextInt(digitalCharacter.length())));

        for (int i = 0; i < 2; i++)
            sb.append(specialCharacter.charAt(rnd.nextInt(specialCharacter.length())));
        return sb.toString();
    }

    private User giveUserARole(final UserDto userDto, User user) {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.size() < 4) {
            roleRepository.deleteAll();
            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            Role regularUser = new Role();
            regularUser.setName("ROLE_USER");
            Role regularManager = new Role();
            regularManager.setName("ROLE_MANAGER");
            Role regularSuperAdmin = new Role();
            regularSuperAdmin.setName("ROLE_SUPER_ADMIN");
            List<Role> roles = new ArrayList<>();
            roles.add(admin);
            roles.add(regularUser);
            roles.add(regularManager);
            roles.add(regularSuperAdmin);
            roleRepository.saveAll(roles);
        } else {
            Role admin = roleRepository.findByName("ROLE_ADMIN");
            Role regularUser = roleRepository.findByName("ROLE_USER");
            Role regularManager = roleRepository.findByName("ROLE_MANAGER");
            Role regularSuperAdmin = roleRepository.findByName("ROLE_SUPER_ADMIN");
            Collection<Role> roles = new ArrayList<>();

            if (userDto.getRole().equalsIgnoreCase(null) || userDto.getRole().isEmpty() || userDto.getRole().equalsIgnoreCase("ROLE_USER".toUpperCase(Locale.ROOT))) {
                roles.add(regularUser);
            } else if (userDto.getRole().equalsIgnoreCase("ROLE_ADMIN".toUpperCase(Locale.ROOT))) {
                roles.add(admin);
                roles.add(regularUser);
            } else if (userDto.getRole().equalsIgnoreCase("ROLE_MANAGER".toUpperCase(Locale.ROOT))) {
                roles.add(regularManager);
                roles.add(regularUser);
                roles.add(admin);
            } else if (userDto.getRole().equalsIgnoreCase("ROLE_SUPER_ADMIN".toUpperCase(Locale.ROOT))) {
                roles.add(regularSuperAdmin);
                roles.add(regularUser);
                roles.add(regularManager);
                roles.add(admin);
            }

            user.setRoles(roles);
        }
        return user;
    }
}
