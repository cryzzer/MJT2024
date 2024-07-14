package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IoTDeviceBaseTest {

    static class TestDevice extends IoTDeviceBase {
        private String id;
        private DeviceType type;

        public TestDevice(String name, double powerConsumption, LocalDateTime installationDateTime, String id, DeviceType type) {
            super(name, powerConsumption, installationDateTime);
            this.id = id;
            this.type = type;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public DeviceType getType() {
            return type;
        }
    }

    @Test
    void testGetPowerConsumptionKWh() {
        LocalDateTime installationDateTime = LocalDateTime.now().minusHours(10);
        TestDevice device = new TestDevice("TestDevice", 5.0, installationDateTime, "ID123", DeviceType.SMART_SPEAKER);

        long expectedConsumption = (long) (10 * 5.0); // 10 hours * 5.0 power consumption
        assertEquals(expectedConsumption, device.getPowerConsumptionKWh());
    }

    @Test
    void testGetRegistrationDuration() {
        LocalDateTime now = LocalDateTime.now();
        TestDevice device = new TestDevice("TestDevice", 5.0, now, "ID123", DeviceType.SMART_SPEAKER);
        device.setRegistration(now.minusHours(5));

        assertEquals(5, device.getRegistration());
    }
}
