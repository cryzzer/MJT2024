package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.EnumMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {
    Map<UtilityType, Double> taxRates;

    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates = taxRates;
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null || billable == null) {
            throw new IllegalArgumentException("utilityType and billable must be not null!");
        }

        return switch (utilityType) {
            case WATER -> taxRates.get(UtilityType.WATER) * billable.getWaterConsumption();
            case ELECTRICITY -> taxRates.get(UtilityType.ELECTRICITY) * billable.getElectricityConsumption();
            case NATURAL_GAS -> taxRates.get(UtilityType.NATURAL_GAS) * billable.getNaturalGasConsumption();
        };
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("billable must be not null!");
        }

        double sum = 0.0d;

        for(UtilityType utilityType : UtilityType.values()){
            sum += getUtilityCosts(utilityType, billable);
        }

        return sum;
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("Both billable1 and billable2 must be not null!");
        }

        Map<UtilityType, Double> costsDifference = new EnumMap<>(UtilityType.class);

        for(UtilityType utilityType : UtilityType.values()){
            double lhs = getUtilityCosts(utilityType, firstBillable);
            double rhs = getUtilityCosts(utilityType, secondBillable);

            double costDifference = Math.abs(lhs - rhs);

            costsDifference.putIfAbsent(utilityType, costDifference);
        }

        return costsDifference;
    }
}
