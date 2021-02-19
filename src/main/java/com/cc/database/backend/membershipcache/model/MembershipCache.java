package com.cc.database.backend.membershipcache.model;

import com.cc.database.backend.membership.model.address.Address;
import com.cc.database.backend.membership.model.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MembershipCache {
    private List<Member> members;
    private Address address;
    private Boolean redLight;

    // Leah's explanation of red light Cache
//    public Member getLoggedInMember(){
//        return Object.requiredNonNull (members.stream)filter(Member::getSelectedMember).findFirst().orElse(null),
//        Message:"Membership cache retuned no logged in person");
//    }
}
/*
member doesnt have access -> true (redlight )
admin diana (false)
admin fincae (true)




serviceImp Test
MoneyServiceImpTest {
    @Rule
private Membership/AdmincacheEntry membershipcacheRedLight;
        }
@mockito

*/




/*
Member (
    Member information
)
Add Member / EditMember / DeleteMember

Member Entity
Member Repo extend Jpa
Member Service (
Add Member / EditMember / DeleteMember
)
Member Controller


AdminOne ()
AdminTwo ()
 */



















