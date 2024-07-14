package bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class KWhComparatorTest {
    @Mock
    private IoTDevice firstDevice;
    @Mock
    private IoTDevice secondDevice;

    private final KWhComparator kWhComparator = new KWhComparator();

    @Test
    void testCompareFirstHasHigherConsumption(){
        when(firstDevice.getPowerConsumptionKWh()).thenReturn(10L);
        when(secondDevice.getPowerConsumptionKWh()).thenReturn(5L);

        int result = kWhComparator.compare(firstDevice, secondDevice);

        assertEquals(-5, result, "First device should have 5 more consumption");
    }

    @Test
    void testCompareSecondHasHigherConsumption(){
        when(firstDevice.getPowerConsumptionKWh()).thenReturn(5L);
        when(secondDevice.getPowerConsumptionKWh()).thenReturn(10L);

        int result = kWhComparator.compare(firstDevice, secondDevice);

        assertEquals(5, result, "Second device should have 5 more consumption");
    }

    @Test
    void testCompareEqualConsumption(){
        when(firstDevice.getPowerConsumptionKWh()).thenReturn(10L);
        when(secondDevice.getPowerConsumptionKWh()).thenReturn(10L);

        int result = kWhComparator.compare(firstDevice, secondDevice);

        assertEquals(0, result, "Both devices should have equal consumption");
    }

}
