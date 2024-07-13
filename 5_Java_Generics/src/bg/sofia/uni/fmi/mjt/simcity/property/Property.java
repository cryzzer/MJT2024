package bg.sofia.uni.fmi.mjt.simcity.property;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.BuildableType;

public abstract class Property implements Billable {
    private final BuildableType type;

    public Property(BuildableType type) {
        this.type = type;
    }

    @Override
    public BuildableType getType() {
        return type;
    }
}
