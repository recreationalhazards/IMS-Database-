package com.example.demo.persistence.model.dto;

import com.example.demo.validation.PasswordMatches;
import com.example.demo.validation.ValidEmail;
import com.example.demo.validation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class UserDto {

    @NotNull
    @Size(min = 1, message = "{Size.userDto.firstName}")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.lastName}")
    private String lastName;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;

    private boolean isUsing2FA;

    private String role;

    private String phoneNumber;

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [firstName=")
                .append(firstName)
                .append(", lastName=")
                .append(lastName)
                .append(", email=")
                .append(email)
                .append(", isUsing2FA=")
                .append(isUsing2FA)
                .append(", role=")
                .append(role).append("]")
                .append(", phoneNumber=")
                .append(phoneNumber);
        return builder.toString();
    }
}
