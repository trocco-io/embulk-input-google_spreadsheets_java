package org.embulk.input.google_spreadsheets_java.typecast;

import static java.math.BigDecimal.ONE;
import static org.embulk.input.google_spreadsheets_java.typecast.TypeCastTest.ONE_SECOND;
import static org.embulk.input.google_spreadsheets_java.typecast.TypeCastTest.TOW_SECONDS;
import static org.embulk.input.google_spreadsheets_java.typecast.TypeCastTest.assertTypeCastException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.function.Function;
import org.embulk.input.google_spreadsheets_java.PluginTask;
import org.embulk.input.google_spreadsheets_java.PluginTaskBuilder;
import org.embulk.util.json.JsonParseException;
import org.embulk.util.rubytime.RubyDateTimeParseException;
import org.junit.Test;
import org.msgpack.value.Value;

public class StrictTypeCastTest {
  private static final PluginTask TASK = PluginTaskBuilder.getDefault().build();
  private static final TypeCast TYPE_CAST = TypeCast.create("strict", TASK);

  @Test
  public void asBoolean() {
    Function<Object, Boolean> asBoolean = TYPE_CAST::asBoolean;
    assertThat(asBoolean.apply(null), is(nullValue()));
    assertThat(asBoolean.apply(""), is(nullValue()));
    // spotless:off
    assertTypeCastException(() -> asBoolean.apply("string"), "Cannot type cast 'string' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertTypeCastException(() -> asBoolean.apply("null"), "Cannot type cast 'null' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertThat(asBoolean.apply(false), is(false));
    assertThat(asBoolean.apply(true), is(true));
    assertThat(asBoolean.apply("False"), is(false));
    assertThat(asBoolean.apply("True"), is(true));
    assertThat(asBoolean.apply("F"), is(false));
    assertThat(asBoolean.apply("T"), is(true));
    assertThat(asBoolean.apply("No"), is(false));
    assertThat(asBoolean.apply("Yes"), is(true));
    assertThat(asBoolean.apply("N"), is(false));
    assertThat(asBoolean.apply("Y"), is(true));
    assertThat(asBoolean.apply("Off"), is(false));
    assertThat(asBoolean.apply("On"), is(true));
    assertThat(asBoolean.apply("0"), is(false));
    assertThat(asBoolean.apply("1"), is(true));
    assertTypeCastException(() -> asBoolean.apply(-100.0), "Cannot type cast '-100' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertTypeCastException(() -> asBoolean.apply(-10.00), "Cannot type cast '-10' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertTypeCastException(() -> asBoolean.apply(-1.10), "Cannot type cast '-1.1' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertTypeCastException(() -> asBoolean.apply(-0.10), "Cannot type cast '-0.1' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertTypeCastException(() -> asBoolean.apply(-1.0), "Cannot type cast '-1' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertThat(asBoolean.apply(-0.0), is(false));
    assertTypeCastException(() -> asBoolean.apply(-1), "Cannot type cast '-1' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertThat(asBoolean.apply(-0), is(false));
    assertThat(asBoolean.apply(0), is(false));
    assertThat(asBoolean.apply(1), is(true));
    assertThat(asBoolean.apply(0.0), is(false));
    assertThat(asBoolean.apply(1.0), is(true));
    assertTypeCastException(() -> asBoolean.apply(0.10), "Cannot type cast '0.1' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertTypeCastException(() -> asBoolean.apply(1.10), "Cannot type cast '1.1' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertTypeCastException(() -> asBoolean.apply(10.00), "Cannot type cast '10' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    assertTypeCastException(() -> asBoolean.apply(100.0), "Cannot type cast '100' to boolean. A boolean value must be one of [true, t, yes, y, on, 1, false, f, no, n, off, 0]");
    // spotless:on
  }

  @Test
  public void asLong() {
    Function<Object, Long> asLong = TYPE_CAST::asLong;
    assertThat(asLong.apply(null), is(nullValue()));
    assertThat(asLong.apply(""), is(nullValue()));
    // spotless:off
    assertTypeCastException(() -> asLong.apply("string"), "Cannot type cast 'string' to number", NumberFormatException.class);
    assertTypeCastException(() -> asLong.apply("null"), "Cannot type cast 'null' to number", NumberFormatException.class);
    assertTypeCastException(() -> asLong.apply(false), "Cannot type cast 'false' of class java.lang.Boolean to long");
    assertTypeCastException(() -> asLong.apply(true), "Cannot type cast 'true' of class java.lang.Boolean to long");
    assertThat(asLong.apply("-100.0"), is(-100L));
    assertThat(asLong.apply("-10.00"), is(-10L));
    assertTypeCastException(() -> asLong.apply("-1.10"), "Cannot narrowing type cast '-1.1' to long");
    assertTypeCastException(() -> asLong.apply("-0.10"), "Cannot narrowing type cast '-0.1' to long");
    assertThat(asLong.apply("-1.0"), is(-1L));
    assertThat(asLong.apply("-0.0"), is(0L));
    assertThat(asLong.apply("-1"), is(-1L));
    assertThat(asLong.apply("-0"), is(0L));
    assertThat(asLong.apply("0"), is(0L));
    assertThat(asLong.apply("1"), is(1L));
    assertThat(asLong.apply("0.0"), is(0L));
    assertThat(asLong.apply("1.0"), is(1L));
    assertTypeCastException(() -> asLong.apply("0.10"), "Cannot narrowing type cast '0.1' to long");
    assertTypeCastException(() -> asLong.apply("1.10"), "Cannot narrowing type cast '1.1' to long");
    assertThat(asLong.apply("10.00"), is(10L));
    assertThat(asLong.apply("100.0"), is(100L));
    assertThat(asLong.apply(-100.0), is(-100L));
    assertThat(asLong.apply(-10.00), is(-10L));
    assertTypeCastException(() -> asLong.apply(-1.10), "Cannot narrowing type cast '-1.1' to long");
    assertTypeCastException(() -> asLong.apply(-0.10), "Cannot narrowing type cast '-0.1' to long");
    assertThat(asLong.apply(-1.0), is(-1L));
    assertThat(asLong.apply(-0.0), is(0L));
    assertThat(asLong.apply(-1), is(-1L));
    assertThat(asLong.apply(-0), is(0L));
    assertThat(asLong.apply(0), is(0L));
    assertThat(asLong.apply(1), is(1L));
    assertThat(asLong.apply(0.0), is(0L));
    assertThat(asLong.apply(1.0), is(1L));
    assertTypeCastException(() -> asLong.apply(0.10), "Cannot narrowing type cast '0.1' to long");
    assertTypeCastException(() -> asLong.apply(1.10), "Cannot narrowing type cast '1.1' to long");
    assertThat(asLong.apply(10.00), is(10L));
    assertThat(asLong.apply(100.0), is(100L));
    assertThat(asLong.apply(new BigDecimal("-100.0")), is(-100L));
    assertThat(asLong.apply(new BigDecimal("-10.00")), is(-10L));
    assertTypeCastException(() -> asLong.apply(new BigDecimal("-1.10")), "Cannot narrowing type cast '-1.1' to long");
    assertTypeCastException(() -> asLong.apply(new BigDecimal("-0.10")), "Cannot narrowing type cast '-0.1' to long");
    assertThat(asLong.apply(new BigDecimal("-1.0")), is(-1L));
    assertThat(asLong.apply(new BigDecimal("-0.0")), is(0L));
    assertThat(asLong.apply(new BigDecimal("-1")), is(-1L));
    assertThat(asLong.apply(new BigDecimal("-0")), is(0L));
    assertThat(asLong.apply(new BigDecimal("0")), is(0L));
    assertThat(asLong.apply(new BigDecimal("1")), is(1L));
    assertThat(asLong.apply(new BigDecimal("0.0")), is(0L));
    assertThat(asLong.apply(new BigDecimal("1.0")), is(1L));
    assertTypeCastException(() -> asLong.apply(new BigDecimal("0.10")), "Cannot narrowing type cast '0.1' to long");
    assertTypeCastException(() -> asLong.apply(new BigDecimal("1.10")), "Cannot narrowing type cast '1.1' to long");
    // spotless:on
    assertThat(asLong.apply(new BigDecimal("10.00")), is(10L));
    assertThat(asLong.apply(new BigDecimal("100.0")), is(100L));
  }

  @Test
  public void asDouble() {
    Function<Object, Double> asDouble = TYPE_CAST::asDouble;
    assertThat(asDouble.apply(null), is(nullValue()));
    assertThat(asDouble.apply(""), is(nullValue()));
    // spotless:off
    assertTypeCastException(() -> asDouble.apply("string"), "Cannot type cast 'string' to number", NumberFormatException.class);
    assertTypeCastException(() -> asDouble.apply("null"), "Cannot type cast 'null' to number", NumberFormatException.class);
    assertTypeCastException(() -> asDouble.apply(false), "Cannot type cast 'false' of class java.lang.Boolean to double");
    assertTypeCastException(() -> asDouble.apply(true), "Cannot type cast 'true' of class java.lang.Boolean to double");
    // spotless:on
    assertThat(asDouble.apply("-100.0"), is(-100.0d));
    assertThat(asDouble.apply("-10.00"), is(-10.0d));
    assertThat(asDouble.apply("-1.10"), is(-1.1d));
    assertThat(asDouble.apply("-0.10"), is(-0.1d));
    assertThat(asDouble.apply("-1.0"), is(-1.0d));
    assertThat(asDouble.apply("-0.0"), is(0.0d));
    assertThat(asDouble.apply("-1"), is(-1.0d));
    assertThat(asDouble.apply("-0"), is(0.0d));
    assertThat(asDouble.apply("0"), is(0.0d));
    assertThat(asDouble.apply("1"), is(1.0d));
    assertThat(asDouble.apply("0.0"), is(0.0d));
    assertThat(asDouble.apply("1.0"), is(1.0d));
    assertThat(asDouble.apply("0.10"), is(0.1d));
    assertThat(asDouble.apply("1.10"), is(1.1d));
    assertThat(asDouble.apply("10.00"), is(10.0d));
    assertThat(asDouble.apply("100.0"), is(100.0d));
    assertThat(asDouble.apply(-100.0), is(-100.0d));
    assertThat(asDouble.apply(-10.00), is(-10.0d));
    assertThat(asDouble.apply(-1.10), is(-1.1d));
    assertThat(asDouble.apply(-0.10), is(-0.1d));
    assertThat(asDouble.apply(-1.0), is(-1.0d));
    assertThat(asDouble.apply(-0.0), is(0.0d));
    assertThat(asDouble.apply(-1), is(-1.0d));
    assertThat(asDouble.apply(-0), is(0.0d));
    assertThat(asDouble.apply(0), is(0.0d));
    assertThat(asDouble.apply(1), is(1.0d));
    assertThat(asDouble.apply(0.0), is(0.0d));
    assertThat(asDouble.apply(1.0), is(1.0d));
    assertThat(asDouble.apply(0.10), is(0.1d));
    assertThat(asDouble.apply(1.10), is(1.1d));
    assertThat(asDouble.apply(10.00), is(10.0d));
    assertThat(asDouble.apply(100.0), is(100.0d));
    assertThat(asDouble.apply(new BigDecimal("-100.0")), is(-100.0d));
    assertThat(asDouble.apply(new BigDecimal("-10.00")), is(-10.0d));
    assertThat(asDouble.apply(new BigDecimal("-1.10")), is(-1.1d));
    assertThat(asDouble.apply(new BigDecimal("-0.10")), is(-0.1d));
    assertThat(asDouble.apply(new BigDecimal("-1.0")), is(-1.0d));
    assertThat(asDouble.apply(new BigDecimal("-0.0")), is(0.0d));
    assertThat(asDouble.apply(new BigDecimal("-1")), is(-1.0d));
    assertThat(asDouble.apply(new BigDecimal("-0")), is(0.0d));
    assertThat(asDouble.apply(new BigDecimal("0")), is(0.0d));
    assertThat(asDouble.apply(new BigDecimal("1")), is(1.0d));
    assertThat(asDouble.apply(new BigDecimal("0.0")), is(0.0d));
    assertThat(asDouble.apply(new BigDecimal("1.0")), is(1.0d));
    assertThat(asDouble.apply(new BigDecimal("0.10")), is(0.1d));
    assertThat(asDouble.apply(new BigDecimal("1.10")), is(1.1d));
    assertThat(asDouble.apply(new BigDecimal("10.00")), is(10.0d));
    assertThat(asDouble.apply(new BigDecimal("100.0")), is(100.0d));
  }

  @Test
  public void asString() {
    Function<Object, String> asString = TYPE_CAST::asString;
    assertThat(asString.apply(null), is(nullValue()));
    assertThat(asString.apply(""), is(nullValue()));
    assertThat(asString.apply("string"), is("string"));
    assertThat(asString.apply("null"), is("null"));
    assertThat(asString.apply(false), is("false"));
    assertThat(asString.apply(true), is("true"));
    assertThat(asString.apply("-100.0"), is("-100.0"));
    assertThat(asString.apply("-10.00"), is("-10.00"));
    assertThat(asString.apply("-1.10"), is("-1.10"));
    assertThat(asString.apply("-0.10"), is("-0.10"));
    assertThat(asString.apply("-1.0"), is("-1.0"));
    assertThat(asString.apply("-0.0"), is("-0.0"));
    assertThat(asString.apply("-1"), is("-1"));
    assertThat(asString.apply("-0"), is("-0"));
    assertThat(asString.apply("0"), is("0"));
    assertThat(asString.apply("1"), is("1"));
    assertThat(asString.apply("0.0"), is("0.0"));
    assertThat(asString.apply("1.0"), is("1.0"));
    assertThat(asString.apply("0.10"), is("0.10"));
    assertThat(asString.apply("1.10"), is("1.10"));
    assertThat(asString.apply("10.00"), is("10.00"));
    assertThat(asString.apply("100.0"), is("100.0"));
    assertThat(asString.apply(-100.0), is("-100"));
    assertThat(asString.apply(-10.00), is("-10"));
    assertThat(asString.apply(-1.10), is("-1.1"));
    assertThat(asString.apply(-0.10), is("-0.1"));
    assertThat(asString.apply(-1.0), is("-1"));
    assertThat(asString.apply(-0.0), is("0"));
    assertThat(asString.apply(-1), is("-1"));
    assertThat(asString.apply(-0), is("0"));
    assertThat(asString.apply(0), is("0"));
    assertThat(asString.apply(1), is("1"));
    assertThat(asString.apply(0.0), is("0"));
    assertThat(asString.apply(1.0), is("1"));
    assertThat(asString.apply(0.10), is("0.1"));
    assertThat(asString.apply(1.10), is("1.1"));
    assertThat(asString.apply(10.00), is("10"));
    assertThat(asString.apply(100.0), is("100"));
    assertThat(asString.apply(new BigDecimal("-100.0")), is("-100"));
    assertThat(asString.apply(new BigDecimal("-10.00")), is("-10"));
    assertThat(asString.apply(new BigDecimal("-1.10")), is("-1.1"));
    assertThat(asString.apply(new BigDecimal("-0.10")), is("-0.1"));
    assertThat(asString.apply(new BigDecimal("-1.0")), is("-1"));
    assertThat(asString.apply(new BigDecimal("-0.0")), is("0"));
    assertThat(asString.apply(new BigDecimal("-1")), is("-1"));
    assertThat(asString.apply(new BigDecimal("-0")), is("0"));
    assertThat(asString.apply(new BigDecimal("0")), is("0"));
    assertThat(asString.apply(new BigDecimal("1")), is("1"));
    assertThat(asString.apply(new BigDecimal("0.0")), is("0"));
    assertThat(asString.apply(new BigDecimal("1.0")), is("1"));
    assertThat(asString.apply(new BigDecimal("0.10")), is("0.1"));
    assertThat(asString.apply(new BigDecimal("1.10")), is("1.1"));
    assertThat(asString.apply(new BigDecimal("10.00")), is("10"));
    assertThat(asString.apply(new BigDecimal("100.0")), is("100"));
  }

  @Test
  public void asTimestamp() {
    // spotless:off
    Function<Object, Function<String, Function<ZoneId, Instant>>> asTimestamp = value -> format -> zone -> TYPE_CAST.asTimestamp(value, format, zone);
    Function<Object, Function<String, Instant>> asTimestamp_ = value -> format -> asTimestamp.apply(value).apply(format).apply(ZoneOffset.UTC);
    Function<Object, Instant> asTimestamp__ = value -> asTimestamp_.apply(value).apply("%Y-%m-%d %H:%M:%S %z");
    assertThat(asTimestamp__.apply(null), is(nullValue()));
    assertThat(asTimestamp__.apply(""), is(nullValue()));
    assertTypeCastException(() -> asTimestamp__.apply("string"), "Cannot type cast 'string' to instant", RubyDateTimeParseException.class);
    assertTypeCastException(() -> asTimestamp__.apply("null"), "Cannot type cast 'null' to instant", RubyDateTimeParseException.class);
    assertTypeCastException(() -> asTimestamp__.apply(false), "Cannot type cast 'false' of class java.lang.Boolean to timestamp");
    assertTypeCastException(() -> asTimestamp__.apply(true), "Cannot type cast 'true' of class java.lang.Boolean to timestamp");
    assertThat(asTimestamp__.apply("1970-01-01 00:00:00 UTC"), is(Instant.parse("1970-01-01T00:00:00Z")));
    assertThat(asTimestamp__.apply("1970-01-01 00:00:00 JST"), is(Instant.parse("1969-12-31T15:00:00Z")));
    assertThat(asTimestamp_.apply("1970-01-01 00:00:00").apply("%Y-%m-%d %H:%M:%S"), is(Instant.parse("1970-01-01T00:00:00Z")));
    assertThat(asTimestamp.apply("1970-01-01 00:00:00").apply("%Y-%m-%d %H:%M:%S").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1969-12-31T15:00:00Z")));
    // spotless:on
    assertThat(asTimestamp_.apply(-100.0).apply("%s"), is(Instant.parse("1969-12-31T23:58:20Z")));
    assertThat(asTimestamp_.apply(-10.00).apply("%s"), is(Instant.parse("1969-12-31T23:59:50Z")));
    assertThat(asTimestamp_.apply(-1.10).apply("%s"), is(Instant.parse("1969-12-31T23:59:59Z")));
    assertThat(asTimestamp_.apply(-0.10).apply("%s"), is(Instant.parse("1970-01-01T00:00:00Z")));
    assertThat(asTimestamp_.apply(-1.0).apply("%s"), is(Instant.parse("1969-12-31T23:59:59Z")));
    assertThat(asTimestamp_.apply(-0.0).apply("%s"), is(Instant.parse("1970-01-01T00:00:00Z")));
    assertThat(asTimestamp_.apply(-1).apply("%s"), is(Instant.parse("1969-12-31T23:59:59Z")));
    assertThat(asTimestamp_.apply(-0).apply("%s"), is(Instant.parse("1970-01-01T00:00:00Z")));
    assertThat(asTimestamp_.apply(0).apply("%s"), is(Instant.parse("1970-01-01T00:00:00Z")));
    assertThat(asTimestamp_.apply(1).apply("%s"), is(Instant.parse("1970-01-01T00:00:01Z")));
    assertThat(asTimestamp_.apply(0.0).apply("%s"), is(Instant.parse("1970-01-01T00:00:00Z")));
    assertThat(asTimestamp_.apply(1.0).apply("%s"), is(Instant.parse("1970-01-01T00:00:01Z")));
    assertThat(asTimestamp_.apply(0.10).apply("%s"), is(Instant.parse("1970-01-01T00:00:00Z")));
    assertThat(asTimestamp_.apply(1.10).apply("%s"), is(Instant.parse("1970-01-01T00:00:01Z")));
    assertThat(asTimestamp_.apply(10.00).apply("%s"), is(Instant.parse("1970-01-01T00:00:10Z")));
    assertThat(asTimestamp_.apply(100.0).apply("%s"), is(Instant.parse("1970-01-01T00:01:40Z")));
    assertThat(asTimestamp_.apply(-100.0).apply(""), is(Instant.parse("1899-09-21T00:00:00Z")));
    assertThat(asTimestamp_.apply(-10.00).apply(""), is(Instant.parse("1899-12-20T00:00:00Z")));
    assertThat(asTimestamp_.apply(-1.10).apply(""), is(Instant.parse("1899-12-28T21:36:00Z")));
    assertThat(asTimestamp_.apply(-0.10).apply(""), is(Instant.parse("1899-12-29T21:36:00Z")));
    assertThat(asTimestamp_.apply(-1.0).apply(""), is(Instant.parse("1899-12-29T00:00:00Z")));
    assertThat(asTimestamp_.apply(-0.0).apply(""), is(Instant.parse("1899-12-30T00:00:00Z")));
    assertThat(asTimestamp_.apply(-1).apply(""), is(Instant.parse("1899-12-29T00:00:00Z")));
    assertThat(asTimestamp_.apply(-0).apply(""), is(Instant.parse("1899-12-30T00:00:00Z")));
    assertThat(asTimestamp_.apply(0).apply(""), is(Instant.parse("1899-12-30T00:00:00Z")));
    assertThat(asTimestamp_.apply(1).apply(""), is(Instant.parse("1899-12-31T00:00:00Z")));
    assertThat(asTimestamp_.apply(0.0).apply(""), is(Instant.parse("1899-12-30T00:00:00Z")));
    assertThat(asTimestamp_.apply(1.0).apply(""), is(Instant.parse("1899-12-31T00:00:00Z")));
    assertThat(asTimestamp_.apply(0.10).apply(""), is(Instant.parse("1899-12-30T02:24:00Z")));
    assertThat(asTimestamp_.apply(1.10).apply(""), is(Instant.parse("1899-12-31T02:24:00Z")));
    assertThat(asTimestamp_.apply(10.00).apply(""), is(Instant.parse("1900-01-09T00:00:00Z")));
    assertThat(asTimestamp_.apply(100.0).apply(""), is(Instant.parse("1900-04-09T00:00:00Z")));
    // spotless:off
    assertThat(ONE.subtract(ONE_SECOND).negate().toPlainString(), is("-0.99998842592592592592592592592592592593"));
    assertThat(ONE_SECOND.negate().toPlainString(), is("-0.00001157407407407407407407407407407407"));
    assertThat(ONE_SECOND.toPlainString(), is("0.00001157407407407407407407407407407407"));
    assertThat(ONE.subtract(ONE_SECOND).toPlainString(), is("0.99998842592592592592592592592592592593"));
    assertThat(asTimestamp_.apply(ONE.subtract(ONE_SECOND).negate()).apply(""), is(Instant.parse("1899-12-29T00:00:01Z")));
    assertThat(asTimestamp_.apply(ONE_SECOND.negate()).apply(""), is(Instant.parse("1899-12-29T23:59:59Z")));
    assertThat(asTimestamp_.apply(ONE_SECOND).apply(""), is(Instant.parse("1899-12-30T00:00:01Z")));
    assertThat(asTimestamp_.apply(ONE.subtract(ONE_SECOND)).apply(""), is(Instant.parse("1899-12-30T23:59:59Z")));
    assertThat(asTimestamp_.apply(-0.99998842592592592592592592592592592593).apply(""), is(Instant.parse("1899-12-29T00:00:01Z")));
    assertThat(asTimestamp_.apply(-0.00001157407407407407407407407407407407).apply(""), is(Instant.parse("1899-12-29T23:59:59Z")));
    assertThat(asTimestamp_.apply(0.00001157407407407407407407407407407407).apply(""), is(Instant.parse("1899-12-30T00:00:01Z")));
    assertThat(asTimestamp_.apply(0.99998842592592592592592592592592592593).apply(""), is(Instant.parse("1899-12-30T23:59:59Z")));
    assertThat(ONE.subtract(TOW_SECONDS).negate().toPlainString(), is("-0.99997685185185185185185185185185185186"));
    assertThat(TOW_SECONDS.negate().toPlainString(), is("-0.00002314814814814814814814814814814814"));
    assertThat(TOW_SECONDS.toPlainString(), is("0.00002314814814814814814814814814814814"));
    assertThat(ONE.subtract(TOW_SECONDS).toPlainString(), is("0.99997685185185185185185185185185185186"));
    assertThat(asTimestamp_.apply(ONE.subtract(TOW_SECONDS).negate()).apply(""), is(Instant.parse("1899-12-29T00:00:02Z")));
    assertThat(asTimestamp_.apply(TOW_SECONDS.negate()).apply(""), is(Instant.parse("1899-12-29T23:59:58Z")));
    assertThat(asTimestamp_.apply(TOW_SECONDS).apply(""), is(Instant.parse("1899-12-30T00:00:02Z")));
    assertThat(asTimestamp_.apply(ONE.subtract(TOW_SECONDS)).apply(""), is(Instant.parse("1899-12-30T23:59:58Z")));
    assertThat(asTimestamp_.apply(-0.99997685185185185185185185185185185186).apply(""), is(Instant.parse("1899-12-29T00:00:02Z")));
    assertThat(asTimestamp_.apply(-0.00002314814814814814814814814814814814).apply(""), is(Instant.parse("1899-12-29T23:59:58Z")));
    assertThat(asTimestamp_.apply(0.00002314814814814814814814814814814814).apply(""), is(Instant.parse("1899-12-30T00:00:02Z")));
    assertThat(asTimestamp_.apply(0.99997685185185185185185185185185185186).apply(""), is(Instant.parse("1899-12-30T23:59:58Z")));
    // spotless:on
  }

  @Test
  public void asJson() {
    Function<Object, Value> asJson = TYPE_CAST::asJson;
    Function<Object, String> toJson = value -> asJson.apply(value).toJson();
    assertThat(asJson.apply(null), is(nullValue()));
    assertThat(asJson.apply(""), is(nullValue()));
    // spotless:off
    assertTypeCastException(() -> toJson.apply("string"), "Cannot type cast 'string' to json", JsonParseException.class);
    // spotless:on
    assertThat(toJson.apply("null"), is("null"));
    assertThat(toJson.apply("\"\""), is("\"\""));
    assertThat(toJson.apply("\"string\""), is("\"string\""));
    assertThat(toJson.apply("\"null\""), is("\"null\""));
    assertThat(toJson.apply("[]"), is("[]"));
    assertThat(toJson.apply("[\"a\",1,\"b\",2,\"c\",3]"), is("[\"a\",1,\"b\",2,\"c\",3]"));
    assertThat(toJson.apply("{}"), is("{}"));
    assertThat(toJson.apply("{\"a\":1,\"b\":2,\"c\":3}"), is("{\"a\":1,\"b\":2,\"c\":3}"));
    assertThat(toJson.apply("false"), is("false"));
    assertThat(toJson.apply("true"), is("true"));
    assertThat(toJson.apply(false), is("false"));
    assertThat(toJson.apply(true), is("true"));
    assertThat(toJson.apply("-100.0"), is("-100.0"));
    assertThat(toJson.apply("-10.00"), is("-10.0"));
    assertThat(toJson.apply("-1.10"), is("-1.1"));
    assertThat(toJson.apply("-0.10"), is("-0.1"));
    assertThat(toJson.apply("-1.0"), is("-1.0"));
    assertThat(toJson.apply("-0.0"), is("-0.0"));
    assertThat(toJson.apply("-1"), is("-1"));
    assertThat(toJson.apply("-0"), is("0"));
    assertThat(toJson.apply("0"), is("0"));
    assertThat(toJson.apply("1"), is("1"));
    assertThat(toJson.apply("0.0"), is("0.0"));
    assertThat(toJson.apply("1.0"), is("1.0"));
    assertThat(toJson.apply("0.10"), is("0.1"));
    assertThat(toJson.apply("1.10"), is("1.1"));
    assertThat(toJson.apply("10.00"), is("10.0"));
    assertThat(toJson.apply("100.0"), is("100.0"));
    assertThat(toJson.apply(-100.0), is("-100"));
    assertThat(toJson.apply(-10.00), is("-10"));
    assertThat(toJson.apply(-1.10), is("-1.1"));
    assertThat(toJson.apply(-0.10), is("-0.1"));
    assertThat(toJson.apply(-1.0), is("-1"));
    assertThat(toJson.apply(-0.0), is("0"));
    assertThat(toJson.apply(-1), is("-1"));
    assertThat(toJson.apply(-0), is("0"));
    assertThat(toJson.apply(0), is("0"));
    assertThat(toJson.apply(1), is("1"));
    assertThat(toJson.apply(0.0), is("0"));
    assertThat(toJson.apply(1.0), is("1"));
    assertThat(toJson.apply(0.10), is("0.1"));
    assertThat(toJson.apply(1.10), is("1.1"));
    assertThat(toJson.apply(10.00), is("10"));
    assertThat(toJson.apply(100.0), is("100"));
  }
}
