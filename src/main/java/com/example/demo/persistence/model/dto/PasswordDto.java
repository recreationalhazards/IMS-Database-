package com.example.demo.persistence.model.dto;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class PasswordDto {

    private String oldPassword;

    private String token;

    private String newPassword;

    private Date expiryDate;

    public PasswordDto() {
        super();
    }

    public void setExpiryDate(final int expiryTimeInMinutes) {
        this.expiryDate = calculateExpiryDate(expiryTimeInMinutes);
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
