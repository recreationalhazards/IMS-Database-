package com.cc.database.backend.membership;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MemberItem {
    private UUID personResourceId;
    private String personNumber;
}
