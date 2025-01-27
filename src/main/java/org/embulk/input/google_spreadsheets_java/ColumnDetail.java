package org.embulk.input.google_spreadsheets_java;

import java.time.ZoneId;
import java.util.List;
import java.util.StringJoiner;
import javax.validation.constraints.NotNull;
import org.embulk.config.ConfigException;
import org.embulk.input.google_spreadsheets_java.typecast.TypeCast;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.Types;
import org.embulk.util.timestamp.LegacyDateTimeZones;

public class ColumnDetail {
  public final int index;
  public final String name;
  public final Type type;
  public final String format;
  public final ZoneId zone;
  public final TypeCast typeCast;

  public ColumnDetail(
      int index,
      @NotNull String name,
      @NotNull Type type,
      @NotNull String format,
      @NotNull String zone,
      @NotNull String typeCast,
      @NotNull PluginTask task) {
    this.index = index;
    this.name = name;
    this.type = type;
    this.format = format;
    this.zone = LegacyDateTimeZones.toZoneId(zone);
    this.typeCast = TypeCast.create(typeCast, task);
  }

  public Object cast(List<Object> record) {
    Object value = index < record.size() ? record.get(index) : null;
    if (Types.BOOLEAN.equals(type)) {
      return typeCast.asBoolean(value);
    } else if (Types.LONG.equals(type)) {
      return typeCast.asLong(value);
    } else if (Types.DOUBLE.equals(type)) {
      return typeCast.asDouble(value);
    } else if (Types.STRING.equals(type)) {
      return typeCast.asString(value);
    } else if (Types.TIMESTAMP.equals(type)) {
      return typeCast.asTimestamp(value, format, zone);
    } else if (Types.JSON.equals(type)) {
      return typeCast.asJson(value);
    } else {
      throw new ConfigException("Unsupported type: " + type);
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", "{", "}")
        .add("index: " + index)
        .add("name: '" + name + "'")
        .add("type: " + type)
        .add("format: '" + format + "'")
        .add("zone: '" + zone + "'")
        .add("typeCast: " + typeCast)
        .toString();
  }
}
