package com.cc.database.backend.postalAdress;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PostalAddress {
    private UUID resourceId;
    private String  street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
