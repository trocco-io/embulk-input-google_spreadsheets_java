package org.embulk.input.google_spreadsheets_java;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.embulk.config.TaskSource;
import org.embulk.util.config.units.ColumnConfig;
import org.embulk.util.config.units.LocalFile;
import org.embulk.util.config.units.SchemaConfig;

public class PluginTaskBuilder {
  private String authMethod;
  private LocalFile jsonKeyfile;
  private String spreadsheetsUrl;
  private String worksheetTitle;
  private int startColumn;
  private int startRow;
  private int endRow;
  private int maxFetchRows;
  private String nullString;
  private List<String> booleanTrueValues;
  private List<String> booleanFalseValues;
  private String valueRenderOption;
  private String dateTimeRenderOption;
  private boolean stopOnInvalidRecord;
  private String defaultTimestampFormat;
  private String defaultTimezone;
  private String defaultTypecast;
  private List<ColumnConfig> columns;

  public static PluginTaskBuilder getDefault() {
    return new PluginTaskBuilder()
        .setAuthMethod("authorized_user")
        .setJsonKeyfile(null)
        .setSpreadsheetsUrl("https://docs.google.com/spreadsheets/d/ID/")
        .setWorksheetTitle("SHEET")
        .setStartColumn(1)
        .setStartRow(1)
        .setEndRow(-1)
        .setMaxFetchRows(10000)
        .setValueRenderOption("FORMATTED_VALUE")
        .setDateTimeRenderOption("SERIAL_NUMBER")
        .setNullString("")
        .setBooleanTrueValues("true", "t", "yes", "y", "on", "1")
        .setBooleanFalseValues("false", "f", "no", "n", "off", "0")
        .setStopOnInvalidRecord(true)
        .setDefaultTimestampFormat("%Y-%m-%d %H:%M:%S.%N %z")
        .setDefaultTimezone("UTC")
        .setDefaultTypecast("strict")
        .setColumns();
  }

  public PluginTaskBuilder setAuthMethod(String authMethod) {
    this.authMethod = authMethod;
    return this;
  }

  public PluginTaskBuilder setJsonKeyfile(LocalFile jsonKeyfile) {
    this.jsonKeyfile = jsonKeyfile;
    return this;
  }

  public PluginTaskBuilder setSpreadsheetsUrl(String spreadsheetsUrl) {
    this.spreadsheetsUrl = spreadsheetsUrl;
    return this;
  }

  public PluginTaskBuilder setWorksheetTitle(String worksheetTitle) {
    this.worksheetTitle = worksheetTitle;
    return this;
  }

  public PluginTaskBuilder setStartColumn(int startColumn) {
    this.startColumn = startColumn;
    return this;
  }

  public PluginTaskBuilder setStartRow(int startRow) {
    this.startRow = startRow;
    return this;
  }

  public PluginTaskBuilder setEndRow(int endRow) {
    this.endRow = endRow;
    return this;
  }

  public PluginTaskBuilder setMaxFetchRows(int maxFetchRows) {
    this.maxFetchRows = maxFetchRows;
    return this;
  }

  public PluginTaskBuilder setNullString(String nullString) {
    this.nullString = nullString;
    return this;
  }

  public PluginTaskBuilder setBooleanTrueValues(String... booleanTrueValues) {
    this.booleanTrueValues = Arrays.asList(booleanTrueValues);
    return this;
  }

  public PluginTaskBuilder setBooleanFalseValues(String... booleanFalseValues) {
    this.booleanFalseValues = Arrays.asList(booleanFalseValues);
    return this;
  }

  public PluginTaskBuilder setValueRenderOption(String valueRenderOption) {
    this.valueRenderOption = valueRenderOption;
    return this;
  }

  public PluginTaskBuilder setDateTimeRenderOption(String dateTimeRenderOption) {
    this.dateTimeRenderOption = dateTimeRenderOption;
    return this;
  }

  public PluginTaskBuilder setStopOnInvalidRecord(boolean stopOnInvalidRecord) {
    this.stopOnInvalidRecord = stopOnInvalidRecord;
    return this;
  }

  public PluginTaskBuilder setDefaultTimestampFormat(String defaultTimestampFormat) {
    this.defaultTimestampFormat = defaultTimestampFormat;
    return this;
  }

  public PluginTaskBuilder setDefaultTimezone(String defaultTimezone) {
    this.defaultTimezone = defaultTimezone;
    return this;
  }

  public PluginTaskBuilder setDefaultTypecast(String defaultTypecast) {
    this.defaultTypecast = defaultTypecast;
    return this;
  }

  public PluginTaskBuilder setColumns(ColumnConfig... columns) {
    this.columns = Arrays.asList(columns);
    return this;
  }

  public PluginTask build() {
    return new PluginTask() {
      private String now =
          LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
              .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

      @Override
      public void validate() {}

      @Override
      @Deprecated
      public TaskSource dump() {
        return null;
      }

      @Override
      public TaskSource toTaskSource() {
        return null;
      }

      @Override
      public ObjectNode toObjectNode() {
        return null;
      }

      @Override
      public String getAuthMethod() {
        return authMethod;
      }

      @Override
      public Optional<LocalFile> getJsonKeyfile() {
        return Optional.ofNullable(jsonKeyfile);
      }

      @Override
      public String getSpreadsheetsUrl() {
        return spreadsheetsUrl;
      }

      @Override
      public String getWorksheetTitle() {
        return worksheetTitle;
      }

      @Override
      public int getStartColumn() {
        return startColumn;
      }

      @Override
      public int getStartRow() {
        return startRow;
      }

      @Override
      public int getEndRow() {
        return endRow;
      }

      @Override
      public int getMaxFetchRows() {
        return maxFetchRows;
      }

      @Override
      public String getNullString() {
        return nullString;
      }

      @Override
      public List<String> getBooleanTrueValues() {
        return booleanTrueValues;
      }

      @Override
      public List<String> getBooleanFalseValues() {
        return booleanFalseValues;
      }

      @Override
      public String getValueRenderOption() {
        return valueRenderOption;
      }

      @Override
      public String getDateTimeRenderOption() {
        return dateTimeRenderOption;
      }

      @Override
      public boolean getStopOnInvalidRecord() {
        return stopOnInvalidRecord;
      }

      @Override
      public String getDefaultTimestampFormat() {
        return defaultTimestampFormat;
      }

      @Override
      public String getDefaultTimezone() {
        return defaultTimezone;
      }

      @Override
      public String getDefaultTypecast() {
        return defaultTypecast;
      }

      @Override
      public SchemaConfig getColumns() {
        return new SchemaConfig(columns);
      }

      @Override
      public String getNow() {
        return now;
      }

      @Override
      public void setNow(String now) {
        this.now = now;
      }
    };
  }
}
