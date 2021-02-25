package com.cc.database.backend.membership.model.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Media;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
// Implements Methods from Member Repository
public class MemberService {
    @Autowired
    private MemberRepo memberRepo;

    // Save New Member
    public ResponseEntity<Member> saveMember(Member member) {
        memberRepo.save(member);
        return new ResponseEntity<Member>(member, HttpStatus.CREATED);
    }

    // Edit Existing Member
    public Response editMember(Member member) {
        Optional<Member> optionalMember = memberRepo.findById(member.getId());
        if (optionalMember.isPresent()) {
            Member tempMember = optionalMember.get();
            tempMember.setId(member.getId());
            tempMember.setFirstName(member.getFirstName());
            tempMember.setLastName(member.getLastName());
            tempMember.setSelectedMember(member.getSelectedMember());
            tempMember.setRedLight(member.getRedLight());
            tempMember.setBirthDate(member.getBirthDate());
            tempMember.setGender(member.getGender());
            tempMember.setMembershipNumber(member.getMembershipNumber());
            tempMember.setRelativeCode(member.getRelativeCode());
            memberRepo.save(tempMember);
            return Response.ok(tempMember).build();
        } else {
            return Response.ok().build();
        }
    }

    // Delete Member
    public Response deleteMember(int id) {
        memberRepo.deleteById(id);
        return Response.ok().build();
    }

    // Find All Members
    public Response findAllMember() {
        List<Member> memberList = memberRepo.findAll();
        return Response.ok(memberList).build();
    }

    // Find Member By Id
    public Response findMemberById(int id) {
        Optional<Member> optionalMember = memberRepo.findById(id);
        if (optionalMember.isPresent())
            return Response.ok(optionalMember.get()).build();
        else
            return Response.ok().build();
    }

    // Find Member By Username
    public Response findMemberByUserName(String firstName) {
        List<Member> memberList = memberRepo.findAll();
        AtomicReference<Member> userOne = new AtomicReference<>(new Member());
        memberList.forEach(user -> {
            if (user.getFirstName().toLowerCase().equals(firstName.toLowerCase())) {
                userOne.set(user);
            }
        });
        if (userOne.get().equals(null))
            return Response.ok().build();
        else
            return Response.ok(userOne.get()).build();
    }
}
