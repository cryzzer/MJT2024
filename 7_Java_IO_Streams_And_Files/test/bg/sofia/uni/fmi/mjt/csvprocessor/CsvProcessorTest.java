package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvProcessorTest {

    private CsvProcessor csvProcessor;
    private Table table;

    @BeforeEach
    void setUp() {
        table = new BaseTable();
        csvProcessor = new CsvProcessor(table);
    }

    @Test
    void testReadCsvValid() throws CsvDataNotCorrectException {
        String csvData = "Name,Age,City\nJohn,30,New York\nJane,25,Los Angeles";
        Reader reader = new StringReader(csvData);
        csvProcessor.readCsv(reader, ",");

        assertEquals(3, table.getRowsCount(), "Expected 3 rows in the table");
        assertEquals(3, table.getColumnNames().size(), "Expected 3 columns in the table");
        assertEquals(2, table.getColumnData("Name").size(), "Expected 2 value in 'Name' column");
        assertEquals(2, table.getColumnData("Age").size(), "Expected 2 value in 'Age' column");
        assertEquals(2, table.getColumnData("City").size(), "Expected 2 value in 'City' column");
    }

    @Test
    void testReadCsvInvalidFormatThrowsException() {
        String csvData = "Name,Age,City\nJohn,30\nJane,25,Los Angeles";
        Reader reader = new StringReader(csvData);

        assertThrows(CsvDataNotCorrectException.class, () -> csvProcessor.readCsv(reader, ","), "Expected exception for invalid CSV format");
    }

    @Test
    void testWriteTableWithDefaultAlignment() throws CsvDataNotCorrectException {
        String csvData = "Name,Age,City\nJohn,30,New York\nJane,25,Los Angeles";
        Reader reader = new StringReader(csvData);
        csvProcessor.readCsv(reader, ",");

        Writer writer = new StringWriter();
        csvProcessor.writeTable(writer);

        String expectedOutput = """
                | Name | Age | City        |
                | ---- | --- | ----------- |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = normalizeNewLines(writer.toString());
        assertEquals(normalizeNewLines(expectedOutput), actualOutput);
    }

    @Test
    void testWriteTableWithLeftAlignment() throws CsvDataNotCorrectException {
        String csvData = "Name,Age,City\nJohn,30,New York\nJane,25,Los Angeles";
        Reader reader = new StringReader(csvData);
        csvProcessor.readCsv(reader, ",");

        Writer writer = new StringWriter();
        csvProcessor.writeTable(writer, ColumnAlignment.LEFT, ColumnAlignment.LEFT, ColumnAlignment.LEFT);

        String expectedOutput = """
                | Name | Age | City        |
                | :--- | :-- | :---------- |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = normalizeNewLines(writer.toString());
        assertEquals(normalizeNewLines(expectedOutput), actualOutput);
    }

    @Test
    void testWriteTableWithRightAlignment() throws CsvDataNotCorrectException {
        String csvData = "Name,Age,City\nJohn,30,New York\nJane,25,Los Angeles";
        Reader reader = new StringReader(csvData);
        csvProcessor.readCsv(reader, ",");

        Writer writer = new StringWriter();
        csvProcessor.writeTable(writer, ColumnAlignment.RIGHT, ColumnAlignment.RIGHT, ColumnAlignment.RIGHT);

        String expectedOutput = """
                | Name | Age | City        |
                | ---: | --: | ----------: |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = normalizeNewLines(writer.toString());
        assertEquals(normalizeNewLines(expectedOutput), actualOutput);
    }

    @Test
    void testWriteTableWithCenterAlignment() throws CsvDataNotCorrectException {
        String csvData = "Name,Age,City\nJohn,30,New York\nJane,25,Los Angeles";
        Reader reader = new StringReader(csvData);
        csvProcessor.readCsv(reader, ",");

        Writer writer = new StringWriter();
        csvProcessor.writeTable(writer, ColumnAlignment.CENTER, ColumnAlignment.CENTER, ColumnAlignment.CENTER);

        String expectedOutput = """
                | Name | Age | City        |
                | :--: | :-: | :---------: |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = normalizeNewLines(writer.toString());
        assertEquals(normalizeNewLines(expectedOutput), actualOutput);
    }

    @Test
    void testWriteTableWithMixedAlignment() throws CsvDataNotCorrectException {
        String csvData = "Name,Age,City\nJohn,30,New York\nJane,25,Los Angeles";
        Reader reader = new StringReader(csvData);
        csvProcessor.readCsv(reader, ",");

        Writer writer = new StringWriter();
        csvProcessor.writeTable(writer, ColumnAlignment.LEFT, ColumnAlignment.CENTER, ColumnAlignment.RIGHT);

        String expectedOutput = """
                | Name | Age | City        |
                | :--- | :-: | ----------: |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = normalizeNewLines(writer.toString());
        assertEquals(normalizeNewLines(expectedOutput), actualOutput);
    }

    private String normalizeNewLines(String input) {
        return input.replace("\r\n", "\n");
    }
}
