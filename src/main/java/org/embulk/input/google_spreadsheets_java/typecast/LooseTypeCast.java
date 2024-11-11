package org.embulk.input.google_spreadsheets_java.typecast;

import java.time.Instant;
import java.time.ZoneId;
import org.embulk.input.google_spreadsheets_java.PluginTask;
import org.embulk.spi.json.JsonValue;

public class LooseTypeCast extends StrictTypeCast {
  protected LooseTypeCast(PluginTask task) {
    super(task);
  }

  @Override
  public Boolean asBoolean(Object value) {
    try {
      return super.asBoolean(value);
    } catch (TypeCastException e) {
      LOGGER.trace("Fallback to null, because of {}", e, e);
      return null;
    }
  }

  @Override
  public Long asLong(Object value) {
    try {
      return super.asLong(value);
    } catch (TypeCastException e) {
      LOGGER.trace("Fallback to null, because of {}", e, e);
      return null;
    }
  }

  @Override
  public Double asDouble(Object value) {
    try {
      return super.asDouble(value);
    } catch (TypeCastException e) {
      LOGGER.trace("Fallback to null, because of {}", e, e);
      return null;
    }
  }

  @Override
  public String asString(Object value) {
    try {
      return super.asString(value);
    } catch (TypeCastException e) {
      LOGGER.trace("Fallback to null, because of {}", e, e);
      return null;
    }
  }

  @Override
  public Instant asTimestamp(Object value, String format, ZoneId zone) {
    try {
      return super.asTimestamp(value, format, zone);
    } catch (TypeCastException e) {
      LOGGER.trace("Fallback to null, because of {}", e, e);
      return null;
    }
  }

  @Override
  public JsonValue asJson(Object value) {
    try {
      return super.asJson(value);
    } catch (TypeCastException e) {
      LOGGER.trace("Fallback to null, because of {}", e, e);
      return null;
    }
  }
}
