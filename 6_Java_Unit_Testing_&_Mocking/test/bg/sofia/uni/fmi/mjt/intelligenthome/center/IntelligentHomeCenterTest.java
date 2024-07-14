package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IntelligentHomeCenterTest {
    @Mock
    private IoTDevice device;
    @Mock
    private IoTDevice device1;
    @Mock
    private IoTDevice device2;
    @Mock
    private IoTDevice device3;
    @Mock
    private DeviceStorage storage;

    @InjectMocks
    private IntelligentHomeCenter homeCenter;

    @Test
    void testRegisterWhenDeviceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.register(null), "Expected exception for device not to be null");
    }

    @Test
    void testRegisterWhenDeviceIsAlreadyRegistered() {
        when(device.getId()).thenReturn("deviceID");
        when(storage.exists("deviceID")).thenReturn(true);

        assertThrows(DeviceAlreadyRegisteredException.class, () -> homeCenter.register(device), "Expected exception for already registered device");
    }

    @Test
    void testRegisterNewDevice() throws DeviceAlreadyRegisteredException {
        when(device.getId()).thenReturn("deviceID");
        when(storage.exists("deviceID")).thenReturn(false);

        homeCenter.register(device);

        verify(device).setRegistration(any(LocalDateTime.class));
        verify(storage).store("deviceID", device);
    }

    @Test
    void testUnregisterWhenDeviceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.unregister(null), "Expected exception for device not to be null");
    }

    @Test
    void testUnregisterWhenDeviceIsNotFound() {
        when(device.getId()).thenReturn("deviceID");
        when(storage.exists("deviceID")).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, () -> homeCenter.unregister(device), "Expected exception for not found device to remove");
    }

    @Test
    void testUnregisterValidDevice() throws DeviceNotFoundException {
        when(device.getId()).thenReturn("deviceID");
        when(storage.exists("deviceID")).thenReturn(true);

        homeCenter.unregister(device);

        verify(storage).delete("deviceID");
    }

    @Test
    void testGetDeviceByIdWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById(null), "Expected exception for id being null");
    }

    @Test
    void testGetDeviceByIdWhenDeviceDoesNotExist() {
        when(storage.exists("deviceID")).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, () -> homeCenter.getDeviceById("deviceID"), "Expected exception for device not being found");
    }

    @Test
    void testGetDeviceByIdExistingDevice() throws DeviceNotFoundException {
        when(storage.exists("deviceID")).thenReturn(true);
        when(storage.get("deviceID")).thenReturn(device);

        assertEquals(device, homeCenter.getDeviceById("deviceID"), "Expected the device return to be equal to this device");

        verify(storage).get("deviceID");
    }

    @Test
    void testGetDeviceQuantityPerTypeWhenDeviceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceQuantityPerType(null), "Expected exception for type being null");
    }

    @Test
    void testGetDeviceQuantityPerTypeWhenNoDevicesOfGivenType() {
        DeviceType deviceType = DeviceType.SMART_SPEAKER;
        when(storage.listAll()).thenReturn(Collections.emptyList());

        int quantity = homeCenter.getDeviceQuantityPerType(deviceType);

        assertEquals(0, quantity, "Expected quantity to be 0 when there are no devices of given type");
    }

    @Test
    void testGetDeviceQuantityPerTypeValid() {
        DeviceType type = DeviceType.SMART_SPEAKER;

        when(device1.getType()).thenReturn(type);
        when(device2.getType()).thenReturn(type);
        when(device3.getType()).thenReturn(DeviceType.BULB);

        List<IoTDevice> devices = Arrays.asList(device1, device2, device3);
        when(storage.listAll()).thenReturn(devices);

        int quantity = homeCenter.getDeviceQuantityPerType(type);

        assertEquals(2, quantity, "Expected quantity to be 2 for devices of type LIGHT");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWhen_N_IsNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getTopNDevicesByPowerConsumption(-1), "Expected exception for 'n' being a negative number");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWhen_N_IsZero() {
        when(storage.listAll()).thenReturn(Collections.emptyList());
        Collection<String> result = homeCenter.getTopNDevicesByPowerConsumption(0);
        assertEquals(Collections.emptyList(), result, "Expected empty collection when n is zero");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWhen_N_IsBiggerThanListSize(){
        when(device1.getId()).thenReturn("device1");
        when(device2.getId()).thenReturn("device2");
        when(device1.getPowerConsumptionKWh()).thenReturn(10L);
        when(device2.getPowerConsumptionKWh()).thenReturn(5L);

        List<IoTDevice> devices = Arrays.asList(device1, device2);
        when(storage.listAll()).thenReturn(devices);

        Collection<String> result = homeCenter.getTopNDevicesByPowerConsumption(5);

        assertEquals(Arrays.asList("device1", "device2"), result, "Expected all device IDs when n is greater than list size");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWhenNIsWithinListSize() {
        when(device1.getId()).thenReturn("device1");
        when(device2.getId()).thenReturn("device2");

        when(device1.getPowerConsumptionKWh()).thenReturn(10L);
        when(device2.getPowerConsumptionKWh()).thenReturn(15L);
        when(device3.getPowerConsumptionKWh()).thenReturn(5L);

        List<IoTDevice> devices = Arrays.asList(device1, device2, device3);
        when(storage.listAll()).thenReturn(devices);

        Collection<String> result = homeCenter.getTopNDevicesByPowerConsumption(2);
        assertEquals(Arrays.asList("device2", "device1"), result, "Expected top 2 device IDs sorted by power consumption");
    }

    @Test
    void testGetFirstNDevicesByRegistrationWhen_N_IsNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getFirstNDevicesByRegistration(-1), "Expected exception for 'n' being a negative number");
    }

    @Test
    void testGetFirstNDevicesByRegistrationWhen_N_IsZero() {
        when(storage.listAll()).thenReturn(Collections.emptyList());
        Collection<IoTDevice> result = homeCenter.getFirstNDevicesByRegistration(0);
        assertEquals(Collections.emptyList(), result, "Expected empty collection when n is zero");
    }

    @Test
    void testGetFirstNDevicesByRegistrationWhen_N_IsBiggerThanListSize(){
        when(device1.getRegistration()).thenReturn(10L);
        when(device2.getRegistration()).thenReturn(5L);

        List<IoTDevice> devices = Arrays.asList(device1, device2);
        when(storage.listAll()).thenReturn(devices);

        Collection<IoTDevice> result = homeCenter.getFirstNDevicesByRegistration(5);

        assertEquals(Arrays.asList(device2, device1), result, "Expected all device IDs when n is greater than list size");
    }

    @Test
    void testGetFirstNDevicesByRegistrationWhenNIsWithinListSize() {
        when(device1.getRegistration()).thenReturn(10L);
        when(device2.getRegistration()).thenReturn(15L);
        when(device3.getRegistration()).thenReturn(5L);

        List<IoTDevice> devices = Arrays.asList(device1, device2, device3);
        when(storage.listAll()).thenReturn(devices);

        Collection<IoTDevice> result = homeCenter.getFirstNDevicesByRegistration(2);
        assertEquals(Arrays.asList(device3, device1), result, "Expected top 2 device IDs sorted by power consumption");
    }
}
