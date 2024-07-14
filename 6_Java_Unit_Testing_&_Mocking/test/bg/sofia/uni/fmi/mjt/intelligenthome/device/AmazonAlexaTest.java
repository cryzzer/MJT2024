package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmazonAlexaTest {

    @BeforeEach
    void resetUniqueNumberDevice() throws Exception {
        Field field = IoTDeviceBase.class.getDeclaredField("uniqueNumberDevice");
        field.setAccessible(true);
        field.set(null, 0);
    }

    @Test
    void testAmazonAlexaInitialization() {
        LocalDateTime now = LocalDateTime.now();
        AmazonAlexa alexa = new AmazonAlexa("LivingRoom", 10.0, now);

        assertEquals("LivingRoom", alexa.getName());
        assertEquals(10.0, alexa.getPowerConsumption());
        assertEquals(DeviceType.SMART_SPEAKER, alexa.getType());
        assertEquals(now, alexa.getInstallationDateTime());
    }

    @Test
    void testAmazonAlexaIdGeneration() {
        LocalDateTime now = LocalDateTime.now();
        AmazonAlexa alexa = new AmazonAlexa("LivingRoom", 10.0, now);

        String expectedId = "SPKR-LivingRoom-0"; // assuming this is the first instance
        assertEquals(expectedId, alexa.getId());
    }
}
