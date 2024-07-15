package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTableTest {

    private Table table;

    @BeforeEach
    void setUp() {
        table = new BaseTable();
    }

    @Test
    void testAddDataAddsHeaders() throws CsvDataNotCorrectException {
        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);

        Collection<String> columnNames = table.getColumnNames();
        assertEquals(3, columnNames.size());
        assertTrue(columnNames.containsAll(List.of(headers)));
    }

    @Test
    void testAddDataAddsRow() throws CsvDataNotCorrectException {
        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);

        String[] row = {"John", "30", "New York"};
        table.addData(row);

        assertEquals(2, table.getRowsCount());

        Collection<String> nameColumnData = table.getColumnData("Name");
        Collection<String> ageColumnData = table.getColumnData("Age");
        Collection<String> cityColumnData = table.getColumnData("City");

        assertEquals(1, nameColumnData.size());
        assertTrue(nameColumnData.contains("John"));

        assertEquals(1, ageColumnData.size());
        assertTrue(ageColumnData.contains("30"));

        assertEquals(1, cityColumnData.size());
        assertTrue(cityColumnData.contains("New York"));
    }

    @Test
    void testAddDataThrowsExceptionForInvalidRowWithMoreData() throws CsvDataNotCorrectException {
        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);

        String[] invalidRow = {"John", "30", "New York", "Extra"};
        assertThrows(CsvDataNotCorrectException.class, () -> table.addData(invalidRow), "Expected exception for row with more data than columns");
    }

    @Test
    void testAddDataThrowsExceptionForInvalidRowWithLessData() throws CsvDataNotCorrectException {
        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);

        String[] invalidRow = {"John", "30"};
        assertThrows(CsvDataNotCorrectException.class, () -> table.addData(invalidRow), "Expected exception for row with less data than columns");
    }

    @Test
    void testGetColumnNames() throws CsvDataNotCorrectException {
        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);

        Collection<String> columnNames = table.getColumnNames();
        assertEquals(3, columnNames.size());
        assertTrue(columnNames.containsAll(List.of(headers)));
    }

    @Test
    void testGetColumnDataThrowsExceptionForNonExistentColumn() {
        assertThrows(IllegalArgumentException.class, () -> table.getColumnData("NonExistentColumn"), "Expected exception for non-existent column");
    }

    @Test
    void testGetColumnDataReturnsEmptyListForEmptyColumn() throws CsvDataNotCorrectException {
        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);

        Collection<String> columnData = table.getColumnData("Name");
        assertEquals(List.of(), columnData, "Expected empty list for column with no data");
    }

    @Test
    void testGetColumnDataReturnsDataForColumn() throws CsvDataNotCorrectException {
        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);

        String[] row = {"John", "30", "New York"};
        table.addData(row);

        Collection<String> columnData = table.getColumnData("Name");
        assertEquals(1, columnData.size());
        assertTrue(columnData.contains("John"), "Expected data for column 'Name'");
    }

    @Test
    void testGetRowsCount() throws CsvDataNotCorrectException {
        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);
        assertEquals(1, table.getRowsCount(), "Expected 1 row count after adding headers");

        String[] row = {"John", "30", "New York"};
        table.addData(row);
        assertEquals(2, table.getRowsCount(), "Expected 2 row count after adding a data row");
    }
}
