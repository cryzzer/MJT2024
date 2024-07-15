package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkdownTablePrinterTest {

    private Table table;
    private TablePrinter printer;

    @BeforeEach
    void setUp() throws CsvDataNotCorrectException {
        table = new BaseTable();
        printer = new MarkdownTablePrinter();

        String[] headers = {"Name", "Age", "City"};
        table.addData(headers);

        String[] row1 = {"John", "30", "New York"};
        String[] row2 = {"Jane", "25", "Los Angeles"};
        table.addData(row1);
        table.addData(row2);
    }

    @Test
    void testPrintTableWithDefaultAlignment() {
        Collection<String> result = printer.printTable(table);

        String expectedOutput = """
                | Name | Age | City        |
                | ---- | --- | ----------- |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = String.join("\n", result);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testPrintTableWithLeftAlignment() {
        Collection<String> result = printer.printTable(table, ColumnAlignment.LEFT, ColumnAlignment.LEFT, ColumnAlignment.LEFT);

        String expectedOutput = """
                | Name | Age | City        |
                | :--- | :-- | :---------- |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = String.join("\n", result);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testPrintTableWithRightAlignment() {
        Collection<String> result = printer.printTable(table, ColumnAlignment.RIGHT, ColumnAlignment.RIGHT, ColumnAlignment.RIGHT);

        String expectedOutput = """
                | Name | Age | City        |
                | ---: | --: | ----------: |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = String.join("\n", result);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testPrintTableWithCenterAlignment() {
        Collection<String> result = printer.printTable(table, ColumnAlignment.CENTER, ColumnAlignment.CENTER, ColumnAlignment.CENTER);

        String expectedOutput = """
                | Name | Age | City        |
                | :--: | :-: | :---------: |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = String.join("\n", result);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testPrintTableWithMixedAlignment() {
        Collection<String> result = printer.printTable(table, ColumnAlignment.LEFT, ColumnAlignment.CENTER, ColumnAlignment.RIGHT);

        String expectedOutput = """
                | Name | Age | City        |
                | :--- | :-: | ----------: |
                | John | 30  | New York    |
                | Jane | 25  | Los Angeles |""";

        String actualOutput = String.join("\n", result);

        assertEquals(expectedOutput, actualOutput);
    }
}
