package org.embulk.input.google_spreadsheets_java.typecast;

import java.time.Instant;
import java.time.ZoneId;
import org.embulk.input.google_spreadsheets_java.PluginTask;
import org.embulk.spi.json.JsonValue;

public class MinimalTypeCast extends TypeCast {
  protected MinimalTypeCast(PluginTask task) {
    super(task);
  }

  @Override
  public Boolean asBoolean(Object value) {
    return nullOr(value, this::asBoolean, this::toBooleanString);
  }

  @Override
  public Long asLong(Object value) {
    return nullOr(value, this::asLong, this::toLongString);
  }

  @Override
  public Double asDouble(Object value) {
    return nullOr(value, this::asDouble, this::toDoubleString);
  }

  @Override
  public String asString(Object value) {
    return nullOr(value, this::asString);
  }

  @Override
  public Instant asTimestamp(Object value, String format, ZoneId zone) {
    return nullOr(value, asTimestamp(format, zone));
  }

  @Override
  public JsonValue asJson(Object value) {
    return nullOr(value, this::asJson);
  }
}
