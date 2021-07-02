package com.example.demo.dto;

import java.util.Calendar;
import java.util.Date;

public class PasswordDto {

    private String oldPassword;

    private String token;

    private String newPassword;

    private Date expiryDate;

    public PasswordDto() {
        super();
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Date getExpiryDate() {
        return expiryDate;
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
