package com.cc.database.member.backend.profile;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Getter

@Setter

public class EmailAddress {
    private UUID relativeId;
    private String address;
}
