package bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationComparatorTest {

    @Mock
    private IoTDevice firstDevice;
    @Mock
    private IoTDevice secondDevice;

    private final RegistrationComparator registrationComparator = new RegistrationComparator();

    @Test
    void testCompareFirstHasHigherConsumption(){
        when(firstDevice.getRegistration()).thenReturn(10L);
        when(secondDevice.getRegistration()).thenReturn(5L);

        int result = registrationComparator.compare(firstDevice, secondDevice);

        assertEquals(5, result, "First device should have 5 more consumption");
    }

    @Test
    void testCompareSecondHasHigherConsumption(){
        when(firstDevice.getRegistration()).thenReturn(5L);
        when(secondDevice.getRegistration()).thenReturn(10L);

        int result = registrationComparator.compare(firstDevice, secondDevice);

        assertEquals(-5, result, "Second device should have 5 more consumption");
    }

    @Test
    void testCompareEqualConsumption(){
        when(firstDevice.getRegistration()).thenReturn(10L);
        when(secondDevice.getRegistration()).thenReturn(10L);

        int result = registrationComparator.compare(firstDevice, secondDevice);

        assertEquals(0, result, "Both devices should have equal consumption");
    }
}
