package com.cc.database.backend.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter

@Setter

public class PersonProfileResponse {
    private UUID resourceId;
    private List<EmailAddress> emailAddresses;
}
