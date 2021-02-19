package com.cc.database.backend.person.model;

import com.cc.database.backend.membership.model.address.Address;
import com.cc.database.backend.phone.PhoneNumber;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Person {
    private UUID resourceId;
    private List<PhoneNumber> phoneNumbers;
    private List <Address> addressList;
    private String name;
}
