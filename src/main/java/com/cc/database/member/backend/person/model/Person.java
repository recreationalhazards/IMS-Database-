package com.cc.database.member.backend.person.model;

import com.cc.database.member.backend.phone.PhoneNumber;
import com.cc.database.member.backend.postalAddress.PostalAddress;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter

public class Person {
    private UUID resourceId;
    private List<PhoneNumber> phoneNumbers;
    private List <PostalAddress> postalAddresses;
    String name;
}
