package org.embulk.input.google_spreadsheets_java.typecast;

import java.time.Instant;
import java.time.ZoneId;
import javax.validation.constraints.NotNull;
import org.embulk.input.google_spreadsheets_java.PluginTask;

public class StrictTypeCast extends MinimalTypeCast {
  protected StrictTypeCast(PluginTask task) {
    super(task);
  }

  @Override
  public Boolean asBoolean(Object value) {
    return nullOr(value, this::asBoolean);
  }

  @Override
  public Long asLong(Object value) {
    if (!(value == null || value instanceof String || value instanceof Number)) {
      throw new TypeCastException(
          "Cannot type cast '" + value + "' of " + value.getClass() + " to long");
    }
    return nullOr(value, this::asLong, this::toDoubleString);
  }

  @Override
  public Double asDouble(Object value) {
    if (!(value == null || value instanceof String || value instanceof Number)) {
      throw new TypeCastException(
          "Cannot type cast '" + value + "' of " + value.getClass() + " to double");
    }
    return super.asDouble(value);
  }

  @Override
  public Instant asTimestamp(Object value, String format, ZoneId zone) {
    if (!(value == null || value instanceof String || value instanceof Number)) {
      throw new TypeCastException(
          "Cannot type cast '" + value + "' of " + value.getClass() + " to timestamp");
    }
    return super.asTimestamp(value, format, zone);
  }

  @Override
  protected Boolean asBoolean(@NotNull String value) {
    if (isBooleanTrue(value)) {
      return true;
    } else if (isBooleanFalse(value)) {
      return false;
    } else {
      throw new TypeCastException(
          "Cannot type cast '"
              + value
              + "' to boolean. A boolean value must be one of "
              + getBooleanValues());
    }
  }

  @Override
  protected Long asLong(@NotNull String value) {
    if (toBigDecimal(value).scale() > 0) {
      throw new TypeCastException("Cannot narrowing type cast '" + value + "' to long");
    }
    return super.asLong(value);
  }
}
