package bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeviceNotFoundExceptionTest {
    @Test
    void testExceptionMessage() {
        String message = "Device is not found";
        DeviceNotFoundException exception = new DeviceNotFoundException(message);

        assertEquals(message, exception.getMessage(), "Exception message should match the provided message");
    }

    @Test
    void testExceptionWithNullMessage() {
        DeviceNotFoundException exception = new DeviceNotFoundException(null);

        assertNull(exception.getMessage(), "Exception message should be null when null is passed to the constructor");
    }
}
