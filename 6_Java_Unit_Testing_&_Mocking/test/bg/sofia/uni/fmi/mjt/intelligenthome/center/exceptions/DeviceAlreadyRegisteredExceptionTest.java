package bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeviceAlreadyRegisteredExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Device is already registered";
        DeviceAlreadyRegisteredException exception = new DeviceAlreadyRegisteredException(message);

        assertEquals(message, exception.getMessage(), "Exception message should match the provided message");
    }

    @Test
    void testExceptionWithNullMessage() {
        DeviceAlreadyRegisteredException exception = new DeviceAlreadyRegisteredException(null);

        assertNull(exception.getMessage(), "Exception message should be null when null is passed to the constructor");
    }
}
