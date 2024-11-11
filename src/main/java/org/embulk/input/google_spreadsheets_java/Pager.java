package org.embulk.input.google_spreadsheets_java;

import static org.embulk.input.google_spreadsheets_java.PagerUtil.toLetters;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.function.Consumer;
import org.embulk.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pager {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final int startColumn;
  private final int startRow;
  private final int endColumn;
  private final int endRow;
  private final int maxFetchRows;
  private final String nullString;

  public Pager(PluginTask task) {
    startColumn = task.getStartColumn();
    startRow = task.getStartRow();
    endColumn = startColumn + task.getColumns().size() - 1;
    endRow = task.getEndRow();
    if ((endRow > 0 && startRow > endRow) || startColumn > endColumn) {
      throw new ConfigException(
          "Area does not exist. Please check start & end for row and column. start row: "
              + startRow
              + ", end row: "
              + endRow
              + ", start column: "
              + startColumn
              + ", end column: "
              + endColumn);
    }
    maxFetchRows = task.getMaxFetchRows();
    if (maxFetchRows < 1) {
      throw new ConfigException("max fetch rows: " + maxFetchRows + " must be positive integer");
    }
    nullString = task.getNullString();
  }

  public void eachRecord(SpreadsheetsClient client, Consumer<List<Object>> consumer) {
    int totalFetchedRows = 0;
    int endRow = getEndRow(client);
    for (int i = startRow; i <= endRow; i += maxFetchRows) {
      String range = getRange(i, Math.min(endRow, i + maxFetchRows - 1));
      int fetchedRows = eachRange(client, consumer, range);
      totalFetchedRows += fetchedRows;
      LOGGER.info(
          "Fetched {} rows in range: '{}' (Total {} rows fetched)",
          fetchedRows,
          range,
          totalFetchedRows);
      if (fetchedRows < maxFetchRows) {
        break;
      }
    }
    if (totalFetchedRows < 1) {
      LOGGER.warn("No data is found");
    }
  }

  private int getEndRow(SpreadsheetsClient client) {
    int maxRow = client.getWorksheetRowCount();
    if (startRow > maxRow) {
      throw new ConfigException(
          "start row: " + startRow + " is larger than spreadsheets max row: " + maxRow);
    }
    if (endRow > maxRow) {
      throw new ConfigException(
          "end row: " + endRow + " is larger than spreadsheets max row: " + maxRow);
    }
    return endRow > 0 ? endRow : maxRow;
  }

  private String getRange(int startRow, int endRow) {
    return toLetters(startColumn) + startRow + ":" + toLetters(endColumn) + endRow;
  }

  private int eachRange(SpreadsheetsClient client, Consumer<List<Object>> consumer, String range) {
    int fetchedRows = 0;
    for (List<Object> record : client.getWorksheetValues(range)) {
      if (endRow < 1 && isEmpty(record)) {
        break;
      }
      consumer.accept(record);
      fetchedRows++;
    }
    return fetchedRows;
  }

  private boolean isEmpty(List<Object> record) {
    return record == null
        || record.isEmpty()
        || record.stream().allMatch(value -> value == null || value.equals(nullString));
  }
}
