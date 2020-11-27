package com.cc.database.member.backend.membership;

import com.cc.database.member.backend.person.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter

@Setter

public class MemberItem {
    private UUID personResourceId;
    private String personNumber;
}
