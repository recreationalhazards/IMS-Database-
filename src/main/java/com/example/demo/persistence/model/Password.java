package com.example.demo.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
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
}
