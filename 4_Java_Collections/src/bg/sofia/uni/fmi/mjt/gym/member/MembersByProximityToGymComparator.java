package bg.sofia.uni.fmi.mjt.gym.member;

import java.util.Comparator;

public class MembersByProximityToGymComparator implements Comparator<GymMember> {
    private final Address gymAddress;

    public MembersByProximityToGymComparator(Address address) {
        this.gymAddress = address;
    }

    @Override
    public int compare(GymMember member1, GymMember member2) {
        return Double.compare(
                member1.getAddress().getDistanceTo(gymAddress),
                member2.getAddress().getDistanceTo(gymAddress));
    }
}
