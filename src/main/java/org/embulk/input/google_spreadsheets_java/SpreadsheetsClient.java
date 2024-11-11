package org.embulk.input.google_spreadsheets_java;

import static org.embulk.input.google_spreadsheets_java.SpreadsheetsUrlUtil.captureId;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpreadsheetsClient {
  private static final String APPLICATION_NAME = "embulk-input-google_spreadsheets_java";
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final String spreadsheetId;
  private final String worksheetTitle;
  private final String valueRenderOption;
  private final String dateTimeRenderOption;
  private final Sheets sheets;
  private final Pager pager;

  public SpreadsheetsClient(PluginTask task, Auth auth, Pager pager) {
    spreadsheetId = captureId(task.getSpreadsheetsUrl());
    worksheetTitle = task.getWorksheetTitle();
    valueRenderOption = task.getValueRenderOption().toUpperCase();
    dateTimeRenderOption = task.getDateTimeRenderOption().toUpperCase();
    sheets = getSheets(auth);
    this.pager = pager;
  }

  public void worksheetEachRecord(Consumer<List<Object>> consumer) {
    pager.eachRecord(this, consumer);
  }

  public List<List<Object>> getWorksheetValues(String range) {
    return getValueRange(
            sheets,
            spreadsheetId,
            worksheetTitle + "!" + range,
            valueRenderOption,
            dateTimeRenderOption)
        .getValues();
  }

  public int getWorksheetRowCount() {
    return getSpreadsheet(sheets, spreadsheetId, worksheetTitle).getSheets().stream()
        .findFirst()
        .map(sheet -> sheet.getProperties().getGridProperties().getRowCount())
        .orElse(-1);
  }

  private static ValueRange getValueRange(
      Sheets sheets,
      String spreadsheetId,
      String range,
      String valueRenderOption,
      String dateTimeRenderOption) {
    try {
      LOGGER.info(
          "Load data from spreadsheet id: '{}', range: '{}', value render option: '{}', date time render option: '{}'",
          spreadsheetId,
          range,
          valueRenderOption,
          dateTimeRenderOption);
      return sheets
          .spreadsheets()
          .values()
          .get(spreadsheetId, range)
          .setValueRenderOption(valueRenderOption)
          .setDateTimeRenderOption(dateTimeRenderOption)
          .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Spreadsheet getSpreadsheet(Sheets sheets, String spreadsheetId, String... ranges) {
    try {
      return sheets
          .spreadsheets()
          .get(spreadsheetId)
          .setRanges(Arrays.asList(ranges))
          .setIncludeGridData(true)
          .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Sheets getSheets(Auth auth) {
    try {
      return new Sheets.Builder(
              GoogleNetHttpTransport.newTrustedTransport(),
              GsonFactory.getDefaultInstance(),
              auth.getHttpRequestInitializer())
          .setApplicationName(APPLICATION_NAME)
          .build();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
