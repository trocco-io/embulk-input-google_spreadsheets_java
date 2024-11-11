package org.embulk.input.google_spreadsheets_java;

import java.util.List;
import java.util.Optional;
import org.embulk.util.config.Config;
import org.embulk.util.config.ConfigDefault;
import org.embulk.util.config.Task;
import org.embulk.util.config.units.LocalFile;
import org.embulk.util.config.units.SchemaConfig;

public interface PluginTask extends Task {
  @Config("auth_method")
  @ConfigDefault("\"authorized_user\"")
  String getAuthMethod();

  @Config("json_keyfile")
  @ConfigDefault("null")
  Optional<LocalFile> getJsonKeyfile();

  @Config("spreadsheets_url")
  String getSpreadsheetsUrl();

  @Config("worksheet_title")
  String getWorksheetTitle();

  @Config("start_column")
  @ConfigDefault("1")
  int getStartColumn();

  @Config("start_row")
  @ConfigDefault("1")
  int getStartRow();

  @Config("end_row")
  @ConfigDefault("-1")
  int getEndRow();

  @Config("max_fetch_rows")
  @ConfigDefault("10000")
  int getMaxFetchRows();

  @Config("null_string")
  @ConfigDefault("\"\"")
  String getNullString();

  // https://yaml.org/type/bool.html
  // https://api.rubyonrails.org/classes/ActiveModel/Type/Boolean.html
  @Config("boolean_true_values")
  @ConfigDefault("[\"true\", \"t\", \"yes\", \"y\", \"on\", \"1\"]")
  List<String> getBooleanTrueValues();

  @Config("boolean_false_values")
  @ConfigDefault("[\"false\", \"f\", \"no\", \"n\", \"off\", \"0\"]")
  List<String> getBooleanFalseValues();

  @Config("value_render_option")
  @ConfigDefault("\"FORMATTED_VALUE\"")
  String getValueRenderOption();

  @Config("date_time_render_option")
  @ConfigDefault("\"SERIAL_NUMBER\"")
  String getDateTimeRenderOption();

  @Config("stop_on_invalid_record")
  @ConfigDefault("true")
  boolean getStopOnInvalidRecord();

  @Config("default_timestamp_format")
  @ConfigDefault("\"%Y-%m-%d %H:%M:%S.%N %z\"")
  String getDefaultTimestampFormat();

  @Config("default_timezone")
  @ConfigDefault("\"UTC\"")
  String getDefaultTimezone();

  @Config("default_typecast")
  @ConfigDefault("\"strict\"")
  String getDefaultTypecast();

  @Config("columns")
  SchemaConfig getColumns();

  String getNow();

  void setNow(String now);
}
