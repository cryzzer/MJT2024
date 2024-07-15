package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class BaseColumnTest {

    private Column column;

    @BeforeEach
    void setUp() {
        column = new BaseColumn();
    }

    @Test
    void testAddData() {
        column.addData("Test Data");
        Collection<String> data = column.getData();
        assertEquals(1, data.size());
        assertEquals("Test Data", data.iterator().next());
    }

    @Test
    void testAddDataNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> column.addData(null), "Expected exception for null data");
    }

    @Test
    void testAddDataEmptyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> column.addData(""), "Expected exception for empty data");
    }

    @Test
    void testGetDataReturnsUnmodifiableCollection() {
        column.addData("Test Data");
        Collection<String> data = column.getData();
        assertThrows(UnsupportedOperationException.class, () -> data.add("New Data"), "Expected exception for modifying unmodifiable collection");
    }

    @Test
    void testGetDataContainsAllAddedValues() {
        column.addData("Data1");
        column.addData("Data2");
        column.addData("Data3");

        Collection<String> data = column.getData();
        assertEquals(3, data.size());
        assertTrue(data.contains("Data1"));
        assertTrue(data.contains("Data2"));
        assertTrue(data.contains("Data3"));
    }
}
