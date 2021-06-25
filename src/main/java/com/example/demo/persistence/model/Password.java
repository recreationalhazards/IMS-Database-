package com.example.demo.persistence.model;

import com.example.demo.validation.ValidPassword;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String oldPassword;

    private String token;

    private String newPassword;

    private Date expiryDate;

    private Boolean isExpired;

    @OneToOne(mappedBy = "password")
    private User user;

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

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
