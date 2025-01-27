package org.embulk.input.google_spreadsheets_java.typecast;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.embulk.config.ConfigException;
import org.embulk.input.google_spreadsheets_java.PluginTask;
import org.embulk.util.timestamp.TimestampFormatter;
import org.msgpack.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TypeCast {
  @SuppressWarnings("deprecation") // TODO: For compatibility with Embulk v0.9
  protected static final org.embulk.util.json.JsonParser PARSER =
      new org.embulk.util.json.JsonParser();

  protected static final BigDecimal SECONDS_PER_DAY = new BigDecimal(60 * 60 * 24);
  protected static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static TypeCast create(String typeCast, PluginTask task) {
    if ("strict".equalsIgnoreCase(typeCast)) {
      return new StrictTypeCast(task);
    } else if ("loose".equalsIgnoreCase(typeCast)) {
      return new LooseTypeCast(task);
    } else if ("minimal".equalsIgnoreCase(typeCast)) {
      return new MinimalTypeCast(task);
    } else {
      throw new ConfigException("Unknown type cast: " + typeCast);
    }
  }

  private final String nullString;
  private final List<String> booleanTrueValues;
  private final List<String> booleanFalseValues;
  private final LocalDateTime now;

  protected TypeCast(PluginTask task) {
    nullString = task.getNullString();
    booleanTrueValues = task.getBooleanTrueValues();
    booleanFalseValues = task.getBooleanFalseValues();
    now = LocalDateTime.parse(task.getNow());
  }

  public abstract Boolean asBoolean(Object value);

  public abstract Long asLong(Object value);

  public abstract Double asDouble(Object value);

  public abstract String asString(Object value);

  public abstract Instant asTimestamp(Object value, String format, ZoneId zone);

  public abstract Value asJson(Object value);

  @Override
  public String toString() {
    return getClass().getSimpleName().replaceFirst("TypeCast$", "").toLowerCase();
  }

  @SafeVarargs
  @SuppressWarnings("varargs")
  protected final <T> T nullOr(
      Object value, @NotNull Function<String, T> function, Function<String, String>... functions) {
    if (value != null) {
      LOGGER.trace("{}: '{}'", value.getClass(), value);
    }
    return value == null || value.equals(nullString)
        ? null
        : Arrays.stream(functions)
            .reduce(Function::compose)
            .orElse(Function.identity())
            .andThen(function)
            .apply(toString(value));
  }

  protected String toString(@NotNull Object value) {
    return value instanceof Number ? toDoubleString(value.toString()) : value.toString();
  }

  protected String toBooleanString(@NotNull String value) {
    return isBooleanFalse(value) ? "false" : "true";
  }

  protected String toLongString(@NotNull String value) {
    return toBigInteger(value).toString();
  }

  protected String toDoubleString(@NotNull String value) {
    return toBigDecimal(value).toPlainString();
  }

  protected BigInteger toBigInteger(@NotNull String value) {
    try {
      return new BigDecimal(value).toBigInteger();
    } catch (Exception e) {
      throw new TypeCastException("Cannot type cast '" + value + "' to number", e);
    }
  }

  protected BigDecimal toBigDecimal(@NotNull String value) {
    try {
      return new BigDecimal(value).stripTrailingZeros();
    } catch (Exception e) {
      throw new TypeCastException("Cannot type cast '" + value + "' to number", e);
    }
  }

  // https://developers.google.com/sheets/api/guides/formats#about_date_time_values
  // https://developers.google.com/sheets/api/reference/rest/v4/DateTimeRenderOption
  protected Instant toInstant(@NotNull String value, @NotNull ZoneId zone) {
    try {
      return LocalDate.of(1899, Month.DECEMBER, 30)
          .atStartOfDay(zone)
          .plusSeconds(longValue(new BigDecimal(value).multiply(SECONDS_PER_DAY)))
          .toInstant();
    } catch (Exception e) {
      throw new TypeCastException("Cannot type cast '" + value + "' to instant", e);
    }
  }

  protected Instant toInstant(
      @NotNull String value,
      @NotNull String format,
      @NotNull ZoneId zone,
      @NotNull ZonedDateTime now) {
    try {
      return TimestampFormatter.builder(format, true)
          .setDefaultZoneId(zone)
          .setDefaultDate(now.getYear(), now.getMonthValue(), now.getDayOfMonth())
          .build()
          .parse(value);
    } catch (Exception e) {
      throw new TypeCastException("Cannot type cast '" + value + "' to instant", e);
    }
  }

  protected Boolean asBoolean(@NotNull String value) {
    try {
      return Boolean.valueOf(value);
    } catch (Exception e) {
      throw new TypeCastException("Cannot type cast '" + value + "' to boolean", e);
    }
  }

  protected Long asLong(@NotNull String value) {
    try {
      return Long.valueOf(value);
    } catch (Exception e) {
      throw new TypeCastException("Cannot type cast '" + value + "' to long", e);
    }
  }

  protected Double asDouble(@NotNull String value) {
    try {
      return Double.valueOf(value);
    } catch (Exception e) {
      throw new TypeCastException("Cannot type cast '" + value + "' to double", e);
    }
  }

  protected String asString(@NotNull String value) {
    return value;
  }

  protected Function<String, Instant> asTimestamp(@NotNull String format, @NotNull ZoneId zone) {
    return value ->
        format.isEmpty()
            ? toInstant(value, zone)
            : toInstant(value, format, zone, now.atZone(zone));
  }

  protected Value asJson(@NotNull String value) {
    try {
      return PARSER.parse(value);
    } catch (Exception e) {
      throw new TypeCastException("Cannot type cast '" + value + "' to json", e);
    }
  }

  protected boolean isBooleanTrue(@NotNull String value) {
    return booleanTrueValues.stream().anyMatch(value::equalsIgnoreCase);
  }

  protected boolean isBooleanFalse(@NotNull String value) {
    return booleanFalseValues.stream().anyMatch(value::equalsIgnoreCase);
  }

  protected List<String> getBooleanValues() {
    return Stream.concat(booleanTrueValues.stream(), booleanFalseValues.stream())
        .collect(Collectors.toList());
  }

  protected static long longValue(BigDecimal value) {
    try {
      LOGGER.trace("value: '{}'", value.toPlainString());
      return value.setScale(3, RoundingMode.HALF_UP).longValueExact();
    } catch (ArithmeticException e) {
      long integer = value.longValue();
      LOGGER.trace("integer: '{}'", integer);
      BigDecimal fraction =
          value.subtract(new BigDecimal(integer)).setScale(3, RoundingMode.HALF_UP);
      LOGGER.trace("fraction: '{}'", fraction.toPlainString());
      return integer
          + fraction
              .subtract(value.signum() < 0 ? BigDecimal.ONE : BigDecimal.ZERO)
              .setScale(0, RoundingMode.HALF_UP)
              .longValueExact();
    }
  }
}
