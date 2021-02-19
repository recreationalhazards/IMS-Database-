package com.cc.database.backend.membership.model.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Member Repository Which Extends Jpa
public interface MemberRepo extends JpaRepository<Member, Integer> {
    public Member findMemberByUserName(String username);
}
