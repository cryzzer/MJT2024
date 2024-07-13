package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {
    private final int initialBuildableArea;

    private int remainingBuildableArea;
    private Map<String, E> buildings;

    public Plot(int buildableArea) {
        this.initialBuildableArea = buildableArea;
        this.remainingBuildableArea = buildableArea;
        buildings = new HashMap<>();
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address should not be null or empty");
        }
        if (buildable == null) {
            throw new IllegalArgumentException("Buildable should not be null");
        }

        if (buildings.containsKey(address)) {
            throw new BuildableAlreadyExistsException(String.format("Address %s is already occupied on the plot", address));
        }

        int requiredArea = buildable.getArea();
        if (requiredArea > getRemainingBuildableArea()) {
            throw new InsufficientPlotAreaException(String.format(
                    "The required area: %d exceeds the remaining plot area: %d.", requiredArea, getRemainingBuildableArea()));
        }

        buildings.put(address, buildable);
        remainingBuildableArea -= requiredArea;
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("Buildables must not me null or empty");
        }

        int lastRemainingBuildableAreaState = this.remainingBuildableArea;
        Map<String, E> lastBuildingsState = new HashMap<>(buildings);

        for (Map.Entry<String, E> entry : buildables.entrySet()) {
            try {
                String address = entry.getKey();
                E buildable = entry.getValue();

                construct(address, buildable);
            } catch (Exception e) {
                this.remainingBuildableArea = lastRemainingBuildableAreaState;
                this.buildings = lastBuildingsState;
                throw e;
            }
        }
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address should not be null or empty");
        }
        if (!buildings.containsKey(address)) {
            throw new BuildableNotFoundException(String.format("Cannot find building with address %s", address));
        }

        Buildable demolishedBuilding = buildings.remove(address);
        int areaOfDemolishedBuilding = demolishedBuilding.getArea();

        remainingBuildableArea += areaOfDemolishedBuilding;
    }

    @Override
    public void demolishAll() {
        remainingBuildableArea = initialBuildableArea;
        buildings.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Map.copyOf(buildings);
    }

    @Override
    public int getRemainingBuildableArea() {
        return remainingBuildableArea;
    }
}
