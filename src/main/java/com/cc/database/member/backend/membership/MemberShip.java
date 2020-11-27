package com.cc.database.member.backend.membership;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter

@Setter

public class MemberShip {
    private UUID resourceId;
    private String membershipId;
    private List<MemberItem> members;
}



