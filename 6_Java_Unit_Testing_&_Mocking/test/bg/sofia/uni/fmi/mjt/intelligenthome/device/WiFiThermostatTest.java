package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WiFiThermostatTest {

    @BeforeEach
    void resetUniqueNumberDevice() throws Exception {
        Field field = IoTDeviceBase.class.getDeclaredField("uniqueNumberDevice");
        field.setAccessible(true);
        field.set(null, 0);
    }

    @Test
    void testWiFiThermostatInitialization() {
        LocalDateTime now = LocalDateTime.now();
        WiFiThermostat thermostat = new WiFiThermostat("Kitchen", 12.0, now);

        assertEquals("Kitchen", thermostat.getName());
        assertEquals(12.0, thermostat.getPowerConsumption());
        assertEquals(DeviceType.THERMOSTAT, thermostat.getType());
        assertEquals(now, thermostat.getInstallationDateTime());
    }

    @Test
    void testWiFiThermostatIdGeneration() {
        LocalDateTime now = LocalDateTime.now();
        WiFiThermostat thermostat = new WiFiThermostat("Kitchen", 12.0, now);

        String expectedId = "TMST-Kitchen-0"; // assuming this is the first instance
        assertEquals(expectedId, thermostat.getId());
    }
}
