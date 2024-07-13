package bg.sofia.uni.fmi.mjt.simcity;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.plot.Plot;
import bg.sofia.uni.fmi.mjt.simcity.property.ResidentialProperty;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;
import bg.sofia.uni.fmi.mjt.simcity.utility.UtilityService;
import bg.sofia.uni.fmi.mjt.simcity.utility.UtilityType;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Plot Tests
        System.out.println("=== Plot Tests ===");

        Plot<Buildable> plot = new Plot<>(1000);

        ResidentialProperty house1 = new ResidentialProperty(200);
        ResidentialProperty house2 = new ResidentialProperty(300);
        ResidentialProperty house3 = new ResidentialProperty(600);

        try {
            // Successfully constructing buildings
            plot.construct("Address1", house1);
            plot.construct("Address2", house2);
            System.out.println("Remaining buildable area: " + plot.getRemainingBuildableArea()); // Expected: 500

            // Attempt to construct a building at an already occupied address
            plot.construct("Address1", house3);
        } catch (BuildableAlreadyExistsException e) {
            System.out.println(e.getMessage()); // Expected: Address Address1 is already occupied on the plot
        } catch (InsufficientPlotAreaException e) {
            System.out.println(e.getMessage());
        }

        try {
            // Attempt to construct a building with insufficient area
            plot.construct("Address3", house3);
        } catch (InsufficientPlotAreaException e) {
            System.out.println(e.getMessage()); // Expected: The required area: 600 exceeds the remaining plot area: 500.
        }

        // Demolish a building
        plot.demolish("Address1");
        System.out.println("Remaining buildable area: " + plot.getRemainingBuildableArea()); // Expected: 700

        try {
            // Attempt to demolish a non-existent building
            plot.demolish("NonExistentAddress");
        } catch (BuildableNotFoundException e) {
            System.out.println(e.getMessage()); // Expected: Cannot find building with address NonExistentAddress
        }

        // Retrieve all buildings
        Map<String, Buildable> allBuildables = plot.getAllBuildables();
        System.out.println("All buildables: " + allBuildables);

        // UtilityService Tests
        System.out.println("=== UtilityService Tests ===");

        Map<UtilityType, Double> taxRates = new HashMap<>();
        taxRates.put(UtilityType.WATER, 1.5);
        taxRates.put(UtilityType.ELECTRICITY, 0.2);
        taxRates.put(UtilityType.NATURAL_GAS, 0.8);

        UtilityService utilityService = new UtilityService(taxRates);

        double waterCost = utilityService.getUtilityCosts(UtilityType.WATER, house1);
        double totalCost = utilityService.getTotalUtilityCosts(house1);

        System.out.println("Water cost for house1: " + waterCost); // Expected: 81.0 (54.00 * 1.5)
        System.out.println("Total utility cost for house1: " + totalCost); // Expected: 399.6 (81.0 + 180.0 + 138.6)
    }
}
