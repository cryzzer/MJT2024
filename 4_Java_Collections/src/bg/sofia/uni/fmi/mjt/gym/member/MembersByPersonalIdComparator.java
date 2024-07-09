package bg.sofia.uni.fmi.mjt.gym.member;

import java.util.Comparator;

public class MembersByPersonalIdComparator implements Comparator<GymMember> {
    @Override
    public int compare(GymMember member1, GymMember member2) {
        return member1.getPersonalIdNumber().compareTo(member2.getPersonalIdNumber());
    }
}
