package bg.sofia.uni.fmi.mjt.gym.member;

import java.util.Comparator;

public class MembersByNameComparator implements Comparator<GymMember> {

    @Override
    public int compare(GymMember member1, GymMember member2) {
        return member1.getName().compareTo(member2.getName());
    }
}
