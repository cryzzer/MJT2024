package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RgbBulbTest {

    @BeforeEach
    void resetUniqueNumberDevice() throws Exception {
        Field field = IoTDeviceBase.class.getDeclaredField("uniqueNumberDevice");
        field.setAccessible(true);
        field.set(null, 0);
    }

    @Test
    void testRgbBulbInitialization() {
        LocalDateTime now = LocalDateTime.now();
        RgbBulb bulb = new RgbBulb("Bedroom", 8.0, now);

        assertEquals("Bedroom", bulb.getName());
        assertEquals(8.0, bulb.getPowerConsumption());
        assertEquals(DeviceType.BULB, bulb.getType());
        assertEquals(now, bulb.getInstallationDateTime());
    }

    @Test
    void testRgbBulbIdGeneration() {
        LocalDateTime now = LocalDateTime.now();
        RgbBulb bulb = new RgbBulb("Bedroom", 8.0, now);

        String expectedId = "BLB-Bedroom-0"; // assuming this is the first instance
        assertEquals(expectedId, bulb.getId());
    }
}
