package com.cc.database.backend.membership.model.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "member")
// Member Entity Class
public class Member {
    // This is XMLBlobData. Group.Membership.ID. in membercache, the individualNumber proptery om a member in entities
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "selectedMember")
    private Boolean selectedMember;
    @Column(name = "redLight")
    private Boolean redLight;
    @Column(name = "birthDate")
    private LocalDate birthDate;
    @Column(name = "gender")
    private String gender;
    @Column(name = "membershipNumber")
    private String membershipNumber;
    @Column(name = "relativeCode")
    private String relativeCode;
}
