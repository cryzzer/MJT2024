package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {
    public double getDistanceTo(Address other) {
        //c^2 = a^2 + b^2
        return Math.sqrt(
                Math.pow((this.longitude - other.longitude), 2)
                + Math.pow((this.latitude - other.latitude), 2)
        );
    }
}
