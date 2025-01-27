package org.embulk.input.google_spreadsheets_java.typecast;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static org.embulk.input.google_spreadsheets_java.typecast.TypeCast.SECONDS_PER_DAY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.embulk.input.google_spreadsheets_java.PluginTask;
import org.embulk.input.google_spreadsheets_java.PluginTaskBuilder;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class TypeCastTest {
  protected static final BigDecimal ONE_SECOND =
      ONE.divide(SECONDS_PER_DAY, MathContext.DECIMAL128);
  protected static final BigDecimal TOW_SECONDS =
      ONE_SECOND.multiply(new BigDecimal(2), MathContext.DECIMAL128);
  protected static final BigDecimal ONE_MINUTE =
      ONE_SECOND.multiply(new BigDecimal(60), MathContext.DECIMAL128);
  protected static final BigDecimal ONE_MILLI =
      ONE_SECOND.divide(new BigDecimal(1000), MathContext.DECIMAL128);
  protected static final MathContext MC = new MathContext(15, RoundingMode.DOWN);
  private static final PluginTask TASK = PluginTaskBuilder.getDefault().build();
  private static final TypeCast TYPE_CAST = TypeCast.create(TASK.getDefaultTypecast(), TASK);

  @Test
  public void toStringWithNumber() {
    Function<Object, String> toString = TYPE_CAST::toString;
    assertThat(String.valueOf(new BigDecimal("-10000000.0")), is("-10000000.0"));
    assertThat(toString.apply(new BigDecimal("-10000000.0")), is("-10000000"));
    assertThat(String.valueOf(new BigDecimal("-1000000.0")), is("-1000000.0"));
    assertThat(toString.apply(new BigDecimal("-1000000.0")), is("-1000000"));
    assertThat(String.valueOf(new BigDecimal("-100000.0")), is("-100000.0"));
    assertThat(toString.apply(new BigDecimal("-100000.0")), is("-100000"));
    assertThat(String.valueOf(new BigDecimal("-10000.0")), is("-10000.0"));
    assertThat(toString.apply(new BigDecimal("-10000.0")), is("-10000"));
    assertThat(String.valueOf(new BigDecimal("-1000.0")), is("-1000.0"));
    assertThat(toString.apply(new BigDecimal("-1000.0")), is("-1000"));
    assertThat(String.valueOf(new BigDecimal("-100.0")), is("-100.0"));
    assertThat(toString.apply(new BigDecimal("-100.0")), is("-100"));
    assertThat(String.valueOf(new BigDecimal("-10.0")), is("-10.0"));
    assertThat(toString.apply(new BigDecimal("-10.0")), is("-10"));
    assertThat(String.valueOf(new BigDecimal("-1.0")), is("-1.0"));
    assertThat(toString.apply(new BigDecimal("-1.0")), is("-1"));
    assertThat(String.valueOf(new BigDecimal("-0.1")), is("-0.1"));
    assertThat(toString.apply(new BigDecimal("-0.1")), is("-0.1"));
    assertThat(String.valueOf(new BigDecimal("-0.01")), is("-0.01"));
    assertThat(toString.apply(new BigDecimal("-0.01")), is("-0.01"));
    assertThat(String.valueOf(new BigDecimal("-0.001")), is("-0.001"));
    assertThat(toString.apply(new BigDecimal("-0.001")), is("-0.001"));
    assertThat(String.valueOf(new BigDecimal("-0.0001")), is("-0.0001"));
    assertThat(toString.apply(new BigDecimal("-0.0001")), is("-0.0001"));
    assertThat(String.valueOf(new BigDecimal("-0.00001")), is("-0.00001"));
    assertThat(toString.apply(new BigDecimal("-0.00001")), is("-0.00001"));
    assertThat(String.valueOf(new BigDecimal("-0.000001")), is("-0.000001"));
    assertThat(toString.apply(new BigDecimal("-0.000001")), is("-0.000001"));
    assertThat(String.valueOf(new BigDecimal("-0.0000001")), is("-1E-7"));
    assertThat(toString.apply(new BigDecimal("-0.0000001")), is("-0.0000001"));
    assertThat(String.valueOf(new BigDecimal("-0.0")), is("0.0"));
    assertThat(toString.apply(new BigDecimal("-0.0")), is("0"));
    assertThat(String.valueOf(new BigDecimal("0.0")), is("0.0"));
    assertThat(toString.apply(new BigDecimal("0.0")), is("0"));
    assertThat(String.valueOf(new BigDecimal("0.0000001")), is("1E-7"));
    assertThat(toString.apply(new BigDecimal("0.0000001")), is("0.0000001"));
    assertThat(String.valueOf(new BigDecimal("0.000001")), is("0.000001"));
    assertThat(toString.apply(new BigDecimal("0.000001")), is("0.000001"));
    assertThat(String.valueOf(new BigDecimal("0.00001")), is("0.00001"));
    assertThat(toString.apply(new BigDecimal("0.00001")), is("0.00001"));
    assertThat(String.valueOf(new BigDecimal("0.0001")), is("0.0001"));
    assertThat(toString.apply(new BigDecimal("0.0001")), is("0.0001"));
    assertThat(String.valueOf(new BigDecimal("0.001")), is("0.001"));
    assertThat(toString.apply(new BigDecimal("0.001")), is("0.001"));
    assertThat(String.valueOf(new BigDecimal("0.01")), is("0.01"));
    assertThat(toString.apply(new BigDecimal("0.01")), is("0.01"));
    assertThat(String.valueOf(new BigDecimal("0.1")), is("0.1"));
    assertThat(toString.apply(new BigDecimal("0.1")), is("0.1"));
    assertThat(String.valueOf(new BigDecimal("1.0")), is("1.0"));
    assertThat(toString.apply(new BigDecimal("1.0")), is("1"));
    assertThat(String.valueOf(new BigDecimal("10.0")), is("10.0"));
    assertThat(toString.apply(new BigDecimal("10.0")), is("10"));
    assertThat(String.valueOf(new BigDecimal("100.0")), is("100.0"));
    assertThat(toString.apply(new BigDecimal("100.0")), is("100"));
    assertThat(String.valueOf(new BigDecimal("1000.0")), is("1000.0"));
    assertThat(toString.apply(new BigDecimal("1000.0")), is("1000"));
    assertThat(String.valueOf(new BigDecimal("10000.0")), is("10000.0"));
    assertThat(toString.apply(new BigDecimal("10000.0")), is("10000"));
    assertThat(String.valueOf(new BigDecimal("100000.0")), is("100000.0"));
    assertThat(toString.apply(new BigDecimal("100000.0")), is("100000"));
    assertThat(String.valueOf(new BigDecimal("1000000.0")), is("1000000.0"));
    assertThat(toString.apply(new BigDecimal("1000000.0")), is("1000000"));
    assertThat(String.valueOf(new BigDecimal("10000000.0")), is("10000000.0"));
    assertThat(toString.apply(new BigDecimal("10000000.0")), is("10000000"));
    assertThat(String.valueOf(-10000000d), is("-1.0E7"));
    assertThat(toString.apply(-10000000d), is("-10000000"));
    assertThat(String.valueOf(-1000000d), is("-1000000.0"));
    assertThat(toString.apply(-1000000d), is("-1000000"));
    assertThat(String.valueOf(-100000d), is("-100000.0"));
    assertThat(toString.apply(-100000d), is("-100000"));
    assertThat(String.valueOf(-10000d), is("-10000.0"));
    assertThat(toString.apply(-10000d), is("-10000"));
    assertThat(String.valueOf(-1000d), is("-1000.0"));
    assertThat(toString.apply(-1000d), is("-1000"));
    assertThat(String.valueOf(-100d), is("-100.0"));
    assertThat(toString.apply(-100d), is("-100"));
    assertThat(String.valueOf(-10d), is("-10.0"));
    assertThat(toString.apply(-10d), is("-10"));
    assertThat(String.valueOf(-1d), is("-1.0"));
    assertThat(toString.apply(-1d), is("-1"));
    assertThat(String.valueOf(-0.1d), is("-0.1"));
    assertThat(toString.apply(-0.1d), is("-0.1"));
    assertThat(String.valueOf(-0.01d), is("-0.01"));
    assertThat(toString.apply(-0.01d), is("-0.01"));
    assertThat(String.valueOf(-0.001d), is("-0.001"));
    assertThat(toString.apply(-0.001d), is("-0.001"));
    assertThat(String.valueOf(-0.0001d), is("-1.0E-4"));
    assertThat(toString.apply(-0.0001d), is("-0.0001"));
    assertThat(String.valueOf(-0.00001d), is("-1.0E-5"));
    assertThat(toString.apply(-0.00001d), is("-0.00001"));
    assertThat(String.valueOf(-0.000001d), is("-1.0E-6"));
    assertThat(toString.apply(-0.000001d), is("-0.000001"));
    assertThat(String.valueOf(-0.0000001d), is("-1.0E-7"));
    assertThat(toString.apply(-0.0000001d), is("-0.0000001"));
    assertThat(String.valueOf(-0d), is("-0.0"));
    assertThat(toString.apply(-0d), is("0"));
    assertThat(String.valueOf(0d), is("0.0"));
    assertThat(toString.apply(0d), is("0"));
    assertThat(String.valueOf(0.0000001d), is("1.0E-7"));
    assertThat(toString.apply(0.0000001d), is("0.0000001"));
    assertThat(String.valueOf(0.000001d), is("1.0E-6"));
    assertThat(toString.apply(0.000001d), is("0.000001"));
    assertThat(String.valueOf(0.00001d), is("1.0E-5"));
    assertThat(toString.apply(0.00001d), is("0.00001"));
    assertThat(String.valueOf(0.0001d), is("1.0E-4"));
    assertThat(toString.apply(0.0001d), is("0.0001"));
    assertThat(String.valueOf(0.001d), is("0.001"));
    assertThat(toString.apply(0.001d), is("0.001"));
    assertThat(String.valueOf(0.01d), is("0.01"));
    assertThat(toString.apply(0.01d), is("0.01"));
    assertThat(String.valueOf(0.1d), is("0.1"));
    assertThat(toString.apply(0.1d), is("0.1"));
    assertThat(String.valueOf(1d), is("1.0"));
    assertThat(toString.apply(1d), is("1"));
    assertThat(String.valueOf(10d), is("10.0"));
    assertThat(toString.apply(10d), is("10"));
    assertThat(String.valueOf(100d), is("100.0"));
    assertThat(toString.apply(100d), is("100"));
    assertThat(String.valueOf(1000d), is("1000.0"));
    assertThat(toString.apply(1000d), is("1000"));
    assertThat(String.valueOf(10000d), is("10000.0"));
    assertThat(toString.apply(10000d), is("10000"));
    assertThat(String.valueOf(100000d), is("100000.0"));
    assertThat(toString.apply(100000d), is("100000"));
    assertThat(String.valueOf(1000000d), is("1000000.0"));
    assertThat(toString.apply(1000000d), is("1000000"));
    assertThat(String.valueOf(10000000d), is("1.0E7"));
    assertThat(toString.apply(10000000d), is("10000000"));
    assertThat(String.valueOf(new BigInteger("-10000000")), is("-10000000"));
    assertThat(toString.apply(new BigInteger("-10000000")), is("-10000000"));
    assertThat(String.valueOf(new BigInteger("-1")), is("-1"));
    assertThat(toString.apply(new BigInteger("-1")), is("-1"));
    assertThat(String.valueOf(new BigInteger("-0")), is("0"));
    assertThat(toString.apply(new BigInteger("-0")), is("0"));
    assertThat(String.valueOf(new BigInteger("0")), is("0"));
    assertThat(toString.apply(new BigInteger("0")), is("0"));
    assertThat(String.valueOf(new BigInteger("1")), is("1"));
    assertThat(toString.apply(new BigInteger("1")), is("1"));
    assertThat(String.valueOf(new BigInteger("10000000")), is("10000000"));
    assertThat(toString.apply(new BigInteger("10000000")), is("10000000"));
    assertThat(String.valueOf(-10000000L), is("-10000000"));
    assertThat(toString.apply(-10000000L), is("-10000000"));
    assertThat(String.valueOf(-1L), is("-1"));
    assertThat(toString.apply(-1L), is("-1"));
    assertThat(String.valueOf(-0L), is("0"));
    assertThat(toString.apply(-0L), is("0"));
    assertThat(String.valueOf(0L), is("0"));
    assertThat(toString.apply(0L), is("0"));
    assertThat(String.valueOf(1L), is("1"));
    assertThat(toString.apply(1L), is("1"));
    assertThat(String.valueOf(10000000L), is("10000000"));
    assertThat(toString.apply(10000000L), is("10000000"));
  }

  @Test
  public void toLongString() {
    Function<String, String> toLongString = TYPE_CAST::toLongString;
    assertThat(toLongString.apply("-1.0"), is("-1"));
    assertThat(toLongString.apply("-0.9"), is("0"));
    assertThat(toLongString.apply("-0.8"), is("0"));
    assertThat(toLongString.apply("-0.7"), is("0"));
    assertThat(toLongString.apply("-0.6"), is("0"));
    assertThat(toLongString.apply("-0.5"), is("0"));
    assertThat(toLongString.apply("-0.4"), is("0"));
    assertThat(toLongString.apply("-0.3"), is("0"));
    assertThat(toLongString.apply("-0.2"), is("0"));
    assertThat(toLongString.apply("-0.1"), is("0"));
    assertThat(toLongString.apply("-0.0"), is("0"));
    assertThat(toLongString.apply("0.0"), is("0"));
    assertThat(toLongString.apply("0.1"), is("0"));
    assertThat(toLongString.apply("0.2"), is("0"));
    assertThat(toLongString.apply("0.3"), is("0"));
    assertThat(toLongString.apply("0.4"), is("0"));
    assertThat(toLongString.apply("0.5"), is("0"));
    assertThat(toLongString.apply("0.6"), is("0"));
    assertThat(toLongString.apply("0.7"), is("0"));
    assertThat(toLongString.apply("0.8"), is("0"));
    assertThat(toLongString.apply("0.9"), is("0"));
    assertThat(toLongString.apply("1.0"), is("1"));
  }

  @Test
  public void longValue() {
    Function<BigDecimal, Long> longValue = TypeCast::longValue;
    assertThat(longValue.apply(new BigDecimal("-1.0")), is(-1L));
    assertThat(longValue.apply(new BigDecimal("-0.9")), is(-2L));
    assertThat(longValue.apply(new BigDecimal("-0.8")), is(-2L));
    assertThat(longValue.apply(new BigDecimal("-0.7")), is(-2L));
    assertThat(longValue.apply(new BigDecimal("-0.6")), is(-2L));
    assertThat(longValue.apply(new BigDecimal("-0.5")), is(-2L));
    assertThat(longValue.apply(new BigDecimal("-0.4")), is(-1L));
    assertThat(longValue.apply(new BigDecimal("-0.3")), is(-1L));
    assertThat(longValue.apply(new BigDecimal("-0.2")), is(-1L));
    assertThat(longValue.apply(new BigDecimal("-0.1")), is(-1L));
    assertThat(longValue.apply(new BigDecimal("-0.0")), is(0L));
    assertThat(longValue.apply(new BigDecimal("0.0")), is(0L));
    assertThat(longValue.apply(new BigDecimal("0.1")), is(0L));
    assertThat(longValue.apply(new BigDecimal("0.2")), is(0L));
    assertThat(longValue.apply(new BigDecimal("0.3")), is(0L));
    assertThat(longValue.apply(new BigDecimal("0.4")), is(0L));
    assertThat(longValue.apply(new BigDecimal("0.5")), is(1L));
    assertThat(longValue.apply(new BigDecimal("0.6")), is(1L));
    assertThat(longValue.apply(new BigDecimal("0.7")), is(1L));
    assertThat(longValue.apply(new BigDecimal("0.8")), is(1L));
    assertThat(longValue.apply(new BigDecimal("0.9")), is(1L));
    assertThat(longValue.apply(new BigDecimal("1.0")), is(1L));
    // spotless:off
    assertThat(new BigDecimal("-0.99998842592592592592592592592592592593").multiply(SECONDS_PER_DAY).toPlainString(), is("-86399.00000000000000000000000000000000035200"));
    assertThat(new BigDecimal("-0.00001157407407407407407407407407407407").multiply(SECONDS_PER_DAY).toPlainString(), is("-0.99999999999999999999999999999999964800"));
    assertThat(new BigDecimal("0.00001157407407407407407407407407407407").multiply(SECONDS_PER_DAY).toPlainString(), is("0.99999999999999999999999999999999964800"));
    assertThat(new BigDecimal("0.99998842592592592592592592592592592593").multiply(SECONDS_PER_DAY).toPlainString(), is("86399.00000000000000000000000000000000035200"));
    assertThat(longValue.apply(new BigDecimal("-0.99998842592592592592592592592592592593").multiply(SECONDS_PER_DAY)), is(-86399L));
    assertThat(longValue.apply(new BigDecimal("-0.00001157407407407407407407407407407407").multiply(SECONDS_PER_DAY)), is(-1L));
    assertThat(longValue.apply(new BigDecimal("0.00001157407407407407407407407407407407").multiply(SECONDS_PER_DAY)), is(1L));
    assertThat(longValue.apply(new BigDecimal("0.99998842592592592592592592592592592593").multiply(SECONDS_PER_DAY)), is(86399L));
    assertThat(new BigDecimal("-0.99997685185185185185185185185185185186").multiply(SECONDS_PER_DAY).toPlainString(), is("-86398.00000000000000000000000000000000070400"));
    assertThat(new BigDecimal("-0.00002314814814814814814814814814814814").multiply(SECONDS_PER_DAY).toPlainString(), is("-1.99999999999999999999999999999999929600"));
    assertThat(new BigDecimal("0.00002314814814814814814814814814814814").multiply(SECONDS_PER_DAY).toPlainString(), is("1.99999999999999999999999999999999929600"));
    assertThat(new BigDecimal("0.99997685185185185185185185185185185186").multiply(SECONDS_PER_DAY).toPlainString(), is("86398.00000000000000000000000000000000070400"));
    assertThat(longValue.apply(new BigDecimal("-0.99997685185185185185185185185185185186").multiply(SECONDS_PER_DAY)), is(-86398L));
    assertThat(longValue.apply(new BigDecimal("-0.00002314814814814814814814814814814814").multiply(SECONDS_PER_DAY)), is(-2L));
    assertThat(longValue.apply(new BigDecimal("0.00002314814814814814814814814814814814").multiply(SECONDS_PER_DAY)), is(2L));
    assertThat(longValue.apply(new BigDecimal("0.99997685185185185185185185185185185186").multiply(SECONDS_PER_DAY)), is(86398L));
    assertThat(new BigDecimal("-123456.79").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666656.00"));
    assertThat(new BigDecimal("-123456.78998842592").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666654.99999948800"));
    assertThat(new BigDecimal("-123456.78902777778").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666572.00000019200"));
    assertThat(new BigDecimal("-123456.78901620371").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666571.00000054400"));
    assertThat(new BigDecimal("-123456.78900462962").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666569.99999916800"));
    assertThat(new BigDecimal("-123456.789").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666569.600"));
    assertThat(new BigDecimal("-123456.78899305555").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666568.99999952000"));
    assertThat(new BigDecimal("-123456.78800925925").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666483.99999920000"));
    assertThat(new BigDecimal("-123456.788").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666483.200"));
    assertThat(new BigDecimal("-123456.78799768518").multiply(SECONDS_PER_DAY).toPlainString(), is("-10666666482.99999955200"));
    assertThat(new BigDecimal("123456.78799768518").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666482.99999955200"));
    assertThat(new BigDecimal("123456.788").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666483.200"));
    assertThat(new BigDecimal("123456.78800925925").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666483.99999920000"));
    assertThat(new BigDecimal("123456.78899305555").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666568.99999952000"));
    assertThat(new BigDecimal("123456.789").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666569.600"));
    assertThat(new BigDecimal("123456.78900462962").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666569.99999916800"));
    assertThat(new BigDecimal("123456.78901620371").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666571.00000054400"));
    assertThat(new BigDecimal("123456.78902777778").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666572.00000019200"));
    assertThat(new BigDecimal("123456.78998842592").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666654.99999948800"));
    assertThat(new BigDecimal("123456.79").multiply(SECONDS_PER_DAY).toPlainString(), is("10666666656.00"));
    assertThat(longValue.apply(new BigDecimal("-123456.79").multiply(SECONDS_PER_DAY)), is(-10666666656L));
    assertThat(longValue.apply(new BigDecimal("-123456.78998842592").multiply(SECONDS_PER_DAY)), is(-10666666655L));
    assertThat(longValue.apply(new BigDecimal("-123456.78902777778").multiply(SECONDS_PER_DAY)), is(-10666666572L));
    assertThat(longValue.apply(new BigDecimal("-123456.78901620371").multiply(SECONDS_PER_DAY)), is(-10666666571L));
    assertThat(longValue.apply(new BigDecimal("-123456.78900462962").multiply(SECONDS_PER_DAY)), is(-10666666570L));
    assertThat(longValue.apply(new BigDecimal("-123456.789").multiply(SECONDS_PER_DAY)), is(-10666666571L));
    assertThat(longValue.apply(new BigDecimal("-123456.78899305555").multiply(SECONDS_PER_DAY)), is(-10666666569L));
    assertThat(longValue.apply(new BigDecimal("-123456.78800925925").multiply(SECONDS_PER_DAY)), is(-10666666484L));
    assertThat(longValue.apply(new BigDecimal("-123456.788").multiply(SECONDS_PER_DAY)), is(-10666666484L));
    assertThat(longValue.apply(new BigDecimal("-123456.78799768518").multiply(SECONDS_PER_DAY)), is(-10666666483L));
    assertThat(longValue.apply(new BigDecimal("123456.78799768518").multiply(SECONDS_PER_DAY)), is(10666666483L));
    assertThat(longValue.apply(new BigDecimal("123456.788").multiply(SECONDS_PER_DAY)), is(10666666483L));
    assertThat(longValue.apply(new BigDecimal("123456.78800925925").multiply(SECONDS_PER_DAY)), is(10666666484L));
    assertThat(longValue.apply(new BigDecimal("123456.78899305555").multiply(SECONDS_PER_DAY)), is(10666666569L));
    assertThat(longValue.apply(new BigDecimal("123456.789").multiply(SECONDS_PER_DAY)), is(10666666570L));
    assertThat(longValue.apply(new BigDecimal("123456.78900462962").multiply(SECONDS_PER_DAY)), is(10666666570L));
    assertThat(longValue.apply(new BigDecimal("123456.78901620371").multiply(SECONDS_PER_DAY)), is(10666666571L));
    assertThat(longValue.apply(new BigDecimal("123456.78902777778").multiply(SECONDS_PER_DAY)), is(10666666572L));
    assertThat(longValue.apply(new BigDecimal("123456.78998842592").multiply(SECONDS_PER_DAY)), is(10666666655L));
    assertThat(longValue.apply(new BigDecimal("123456.79").multiply(SECONDS_PER_DAY)), is(10666666656L));
    // spotless:on
  }

  @Test
  public void toInstant() {
    Function<String, Function<ZoneId, Instant>> toInstant =
        value -> zone -> TYPE_CAST.toInstant(value, zone);
    Function<String, Instant> toInstant_ = value -> toInstant.apply(value).apply(ZoneOffset.UTC);
    assertThat(toInstant_.apply("0"), is(Instant.parse("1899-12-30T00:00:00Z")));
    assertThat(toInstant_.apply("0.1"), is(Instant.parse("1899-12-30T02:24:00Z")));
    assertThat(toInstant_.apply("1.1"), is(Instant.parse("1899-12-31T02:24:00Z")));
    assertThat(toInstant_.apply("2.5"), is(Instant.parse("1900-01-01T12:00:00Z")));
    assertThat(toInstant_.apply("33.625"), is(Instant.parse("1900-02-01T15:00:00Z")));
    assertThat(toInstant_.apply("123456.79"), is(Instant.parse("2238-01-03T18:57:36Z")));
    assertThat(toInstant_.apply("123456.78998842592"), is(Instant.parse("2238-01-03T18:57:35Z")));
    assertThat(toInstant_.apply("123456.78902777778"), is(Instant.parse("2238-01-03T18:56:12Z")));
    assertThat(toInstant_.apply("123456.78901620371"), is(Instant.parse("2238-01-03T18:56:11Z")));
    assertThat(toInstant_.apply("123456.78900462962"), is(Instant.parse("2238-01-03T18:56:10Z")));
    assertThat(toInstant_.apply("123456.789"), is(Instant.parse("2238-01-03T18:56:10Z")));
    assertThat(toInstant_.apply("123456.78899305555"), is(Instant.parse("2238-01-03T18:56:09Z")));
    assertThat(toInstant_.apply("123456.78800925925"), is(Instant.parse("2238-01-03T18:54:44Z")));
    assertThat(toInstant_.apply("123456.788"), is(Instant.parse("2238-01-03T18:54:43Z")));
    assertThat(toInstant_.apply("123456.78799768518"), is(Instant.parse("2238-01-03T18:54:43Z")));
    assertThat(toInstant_.apply("-123456.78799768518"), is(Instant.parse("1561-12-25T05:05:17Z")));
    assertThat(toInstant_.apply("-123456.788"), is(Instant.parse("1561-12-25T05:05:16Z")));
    assertThat(toInstant_.apply("-123456.78800925925"), is(Instant.parse("1561-12-25T05:05:16Z")));
    assertThat(toInstant_.apply("-123456.78899305555"), is(Instant.parse("1561-12-25T05:03:51Z")));
    assertThat(toInstant_.apply("-123456.789"), is(Instant.parse("1561-12-25T05:03:49Z")));
    assertThat(toInstant_.apply("-123456.78900462962"), is(Instant.parse("1561-12-25T05:03:50Z")));
    assertThat(toInstant_.apply("-123456.78901620371"), is(Instant.parse("1561-12-25T05:03:49Z")));
    assertThat(toInstant_.apply("-123456.78902777778"), is(Instant.parse("1561-12-25T05:03:48Z")));
    assertThat(toInstant_.apply("-123456.78998842592"), is(Instant.parse("1561-12-25T05:02:25Z")));
    assertThat(toInstant_.apply("-123456.79"), is(Instant.parse("1561-12-25T05:02:24Z")));
    // spotless:off
    assertThat(toInstant.apply("0").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1899-12-29T15:00:00Z")));
    assertThat(toInstant.apply("0.1").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1899-12-29T17:24:00Z")));
    assertThat(toInstant.apply("1.1").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1899-12-30T17:24:00Z")));
    assertThat(toInstant.apply("2.5").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1900-01-01T03:00:00Z")));
    assertThat(toInstant.apply("33.625").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1900-02-01T06:00:00Z")));
    assertThat(toInstant.apply("123456.79").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:57:36Z")));
    assertThat(toInstant.apply("123456.78998842592").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:57:35Z")));
    assertThat(toInstant.apply("123456.78902777778").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:56:12Z")));
    assertThat(toInstant.apply("123456.78901620371").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:56:11Z")));
    assertThat(toInstant.apply("123456.78900462962").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:56:10Z")));
    assertThat(toInstant.apply("123456.789").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:56:10Z")));
    assertThat(toInstant.apply("123456.78899305555").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:56:09Z")));
    assertThat(toInstant.apply("123456.78800925925").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:54:44Z")));
    assertThat(toInstant.apply("123456.788").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:54:43Z")));
    assertThat(toInstant.apply("123456.78799768518").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("2238-01-03T09:54:43Z")));
    assertThat(toInstant.apply("-123456.78799768518").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:05:17Z")));
    assertThat(toInstant.apply("-123456.788").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:05:16Z")));
    assertThat(toInstant.apply("-123456.78800925925").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:05:16Z")));
    assertThat(toInstant.apply("-123456.78899305555").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:03:51Z")));
    assertThat(toInstant.apply("-123456.789").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:03:49Z")));
    assertThat(toInstant.apply("-123456.78900462962").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:03:50Z")));
    assertThat(toInstant.apply("-123456.78901620371").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:03:49Z")));
    assertThat(toInstant.apply("-123456.78902777778").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:03:48Z")));
    assertThat(toInstant.apply("-123456.78998842592").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:02:25Z")));
    assertThat(toInstant.apply("-123456.79").apply(ZoneId.of("Asia/Tokyo")), is(Instant.parse("1561-12-24T20:02:24Z")));
    // spotless:on
  }

  @Test
  public void serials() {
    List<Instant[]> serialInstants = new ArrayList<>();
    List<Instant[]> nonSerialInstants = new ArrayList<>();
    List<BigDecimal[]> serialDecimals = new ArrayList<>();
    List<BigDecimal[]> nonSerialDecimals = new ArrayList<>();
    Function<Object, Instant> toInstant =
        value -> TYPE_CAST.toInstant(TYPE_CAST.toString(value), ZoneOffset.UTC);
    Instant[] instants = {toInstant.apply(0), toInstant.apply(0)};
    BigDecimal[] decimals = {ZERO, ZERO};
    while (decimals[1].compareTo(ONE_MINUTE) < 0) {
      instants[1] = toInstant.apply(decimals[1].round(MC));
      if (instants[0].isBefore(instants[1])) {
        serialInstants.add(instants.clone());
        serialDecimals.add(decimals.clone());
      }
      if (instants[0].isAfter(instants[1])) {
        nonSerialInstants.add(instants.clone());
        nonSerialDecimals.add(decimals.clone());
      }
      instants[0] = instants[1];
      decimals[0] = decimals[1];
      decimals[1] = decimals[1].add(ONE_MILLI);
    }
    assertThat(serialInstants, is(hasSize(60)));
    assertThat(ISO_INSTANT.format(serialInstants.get(0)[0]), is("1899-12-30T00:00:00Z"));
    assertThat(ISO_INSTANT.format(serialInstants.get(0)[1]), is("1899-12-30T00:00:01Z"));
    assertThat(ISO_INSTANT.format(serialInstants.get(59)[0]), is("1899-12-30T00:00:59Z"));
    assertThat(ISO_INSTANT.format(serialInstants.get(59)[1]), is("1899-12-30T00:01:00Z"));
    serialInstants.forEach(TypeCastTest::assertIncrease);
    assertThat(serialDecimals, is(hasSize(60)));
    // spotless:off
    assertThat(serialDecimals.get(0)[0].toPlainString(), is("0.00000577546296296296296296296296296296093"));
    assertThat(serialDecimals.get(0)[1].toPlainString(), is("0.00000578703703703703703703703703703703500"));
    assertThat(serialDecimals.get(59)[0].toPlainString(), is("0.00068864583333333333333333333333333309093"));
    assertThat(serialDecimals.get(59)[1].toPlainString(), is("0.00068865740740740740740740740740740716500"));
    // spotless:on
    serialDecimals.forEach(TypeCastTest::assertIncrease);
    assertThat(nonSerialInstants, is(empty()));
    assertThat(nonSerialDecimals, is(empty()));
  }

  @Test
  public void nonSerials() {
    List<Instant[]> serialInstants = new ArrayList<>();
    List<Instant[]> nonSerialInstants = new ArrayList<>();
    List<BigDecimal[]> serialDecimals = new ArrayList<>();
    List<BigDecimal[]> nonSerialDecimals = new ArrayList<>();
    Function<Object, Instant> toInstant =
        value -> TYPE_CAST.toInstant(TYPE_CAST.toString(value), ZoneOffset.UTC);
    Instant[] instants = {toInstant.apply(0), toInstant.apply(0)};
    BigDecimal[] decimals = {ZERO, ZERO};
    while (decimals[1].compareTo(ONE_MINUTE.negate()) > 0) {
      instants[1] = toInstant.apply(decimals[1].round(MC));
      if (instants[0].isAfter(instants[1])) {
        serialInstants.add(instants.clone());
        serialDecimals.add(decimals.clone());
      }
      if (instants[0].isBefore(instants[1])) {
        nonSerialInstants.add(instants.clone());
        nonSerialDecimals.add(decimals.clone());
      }
      instants[0] = instants[1];
      decimals[0] = decimals[1];
      decimals[1] = decimals[1].add(ONE_MILLI.negate());
    }
    assertThat(serialInstants, is(hasSize(120)));
    assertThat(ISO_INSTANT.format(serialInstants.get(0)[0]), is("1899-12-30T00:00:00Z"));
    assertThat(ISO_INSTANT.format(serialInstants.get(0)[1]), is("1899-12-29T23:59:59Z"));
    assertThat(ISO_INSTANT.format(serialInstants.get(119)[0]), is("1899-12-29T23:59:00Z"));
    assertThat(ISO_INSTANT.format(serialInstants.get(119)[1]), is("1899-12-29T23:58:59Z"));
    serialInstants.forEach(TypeCastTest::assertDecrease);
    assertThat(serialDecimals, is(hasSize(120)));
    // spotless:off
    assertThat(serialDecimals.get(0)[0].toPlainString(), is("0"));
    assertThat(serialDecimals.get(0)[1].toPlainString(), is("-0.00000001157407407407407407407407407407407"));
    assertThat(serialDecimals.get(119)[0].toPlainString(), is("-0.00068864583333333333333333333333333309093"));
    assertThat(serialDecimals.get(119)[1].toPlainString(), is("-0.00068865740740740740740740740740740716500"));
    // spotless:on
    serialDecimals.forEach(TypeCastTest::assertDecrease);
    assertThat(nonSerialInstants, is(hasSize(59)));
    assertThat(ISO_INSTANT.format(nonSerialInstants.get(0)[0]), is("1899-12-29T23:59:58Z"));
    assertThat(ISO_INSTANT.format(nonSerialInstants.get(0)[1]), is("1899-12-29T23:59:59Z"));
    assertThat(ISO_INSTANT.format(nonSerialInstants.get(58)[0]), is("1899-12-29T23:59:00Z"));
    assertThat(ISO_INSTANT.format(nonSerialInstants.get(58)[1]), is("1899-12-29T23:59:01Z"));
    nonSerialInstants.forEach(TypeCastTest::assertIncrease);
    assertThat(nonSerialDecimals, is(hasSize(59)));
    // spotless:off
    assertThat(nonSerialDecimals.get(0)[0].toPlainString(), is("-0.00001156249999999999999999999999999999593"));
    assertThat(nonSerialDecimals.get(0)[1].toPlainString(), is("-0.00001157407407407407407407407407407407000"));
    assertThat(nonSerialDecimals.get(58)[0].toPlainString(), is("-0.00068285879629629629629629629629629605593"));
    assertThat(nonSerialDecimals.get(58)[1].toPlainString(), is("-0.00068287037037037037037037037037037013000"));
    // spotless:on
    nonSerialDecimals.forEach(TypeCastTest::assertDecrease);
  }

  @Test
  public void dateTime() {
    IntStream.rangeClosed(-3000, 3000).forEach(TypeCastTest::assertDateTime);
  }

  private static void assertDateTime(int year) {
    assertDateTime(year, false);
    assertDateTime(year, true);
  }

  private static void assertDateTime(int year, boolean jst) {
    assertInstant(year, jst);
    assertSimpleDateFormat(year, jst);
    assertDateTimeFormatter(year, jst);
  }

  private static void assertInstant(int year, boolean jst) {
    Function<Integer, String> f = y -> String.format("%s%04d", y < 0 ? "-" : "", Math.abs(y));
    assertInstant(
        String.format(
            "%d",
            ChronoUnit.DAYS.between(
                Instant.parse("1899-12-30T00:00:00Z"),
                Instant.parse(String.format("%s-01-01T00:00:00Z", f.apply(year))))),
        jst ? ZoneId.of("Asia/Tokyo") : ZoneOffset.UTC,
        String.format(
            "%s-%02d-%02dT%02d:00:00Z",
            f.apply(jst ? year - 1 : year), jst ? 12 : 1, jst ? 31 : 1, jst ? 15 : 0));
  }

  private static void assertInstant(String value, ZoneId zone, String expected) {
    assertThat(TYPE_CAST.toInstant(value, zone), is(Instant.parse(expected)));
  }

  private static void assertSimpleDateFormat(int year, boolean jst) {
    int offset = jst ? 900 : 0;
    int y = year < 1 ? Math.abs(1 - year) : year;
    // spotless:off
    assertSimpleDateFormat(
        String.format("%04d-01-01 00:00:00 +%04d", year, offset),
        jst ? ZoneId.of("Asia/Tokyo") : ZoneOffset.UTC,
        String.format(
            "%04d-%02d-%02d 00:00:00 +%04d",
            year < 0 || y < 301 ? y : y < 1583 ? y - 1 : y,
            year < 0 || y < 301 ? 1 : y < 1583 ? 12 : 1,
            y < 101
                ? 3
                : y < 201
                    ? year < 0 ? 4 : 2
                    : y < 301
                        ? year < 0 ? 5 : 1
                        : y < 501
                            ? year < 0 ? 6 : 31
                            : y < 601
                                ? year < 0 ? 7 : 30
                                : y < 701
                                    ? year < 0 ? 8 : 29
                                    : y < 901
                                        ? year < 0 ? 9 : 28
                                        : y < 1001
                                            ? year < 0 ? 10 : 27
                                            : y < 1101
                                                ? year < 0 ? 11 : 26
                                                : y < 1301
                                                    ? year < 0 ? 12 : 25
                                                    : y < 1401
                                                        ? year < 0 ? 13 : 24
                                                        : y < 1501
                                                            ? year < 0 ? 14 : 23
                                                            : y < 1583
                                                                ? year < 0 ? 15 : 22
                                                                : year < 0
                                                                    ? y < 1701
                                                                        ? 15
                                                                        : y < 1801
                                                                            ? 16
                                                                            : y < 1901
                                                                                ? 17
                                                                                : y < 2101
                                                                                    ? 18
                                                                                    : y < 2201
                                                                                        ? 19
                                                                                        : y < 2301
                                                                                            ? 20
                                                                                            : y < 2501
                                                                                                ? 21
                                                                                                : y < 2601
                                                                                                    ? 22
                                                                                                    : y < 2701
                                                                                                        ? 23
                                                                                                        : y < 2901
                                                                                                            ? 24
                                                                                                            : y < 3001
                                                                                                                ? 25
                                                                                                                : 26
                                                                    : 1,
            offset));
    // spotless:on
  }

  private static void assertSimpleDateFormat(String value, ZoneId zone, String expected) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    format.setTimeZone(TimeZone.getTimeZone(zone));
    assertThat(
        format.format(
            Date.from(
                TYPE_CAST.toInstant(value, "%Y-%m-%d %H:%M:%S %z", zone, ZonedDateTime.now(zone)))),
        is(expected));
  }

  private static void assertDateTimeFormatter(int year, boolean jst) {
    int offset = jst ? 900 : 0;
    assertDateTimeFormatter(
        String.format("%04d-01-01 00:00:00 +%04d", year, offset),
        jst ? ZoneId.of("Asia/Tokyo") : ZoneOffset.UTC,
        String.format(
            "%04d-01-01 00:%02d:%02d +%04d",
            year < 1 ? Math.abs(1 - year) : year,
            jst && year < 1888 ? 18 : 0,
            jst && year < 1888 ? 59 : 0,
            jst && year < 1888 ? offset + 18 : offset));
  }

  private static void assertDateTimeFormatter(String value, ZoneId zone, String expected) {
    assertThat(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
            .withZone(zone)
            .format(
                TYPE_CAST.toInstant(value, "%Y-%m-%d %H:%M:%S %z", zone, ZonedDateTime.now(zone))),
        is(expected));
  }

  private static void assertIncrease(Instant[] instants) {
    assertThat(ChronoUnit.SECONDS.between(instants[0], instants[1]), is(1L));
  }

  private static void assertDecrease(Instant[] instants) {
    assertThat(ChronoUnit.SECONDS.between(instants[1], instants[0]), is(1L));
  }

  private static void assertIncrease(BigDecimal[] decimals) {
    assertThat(decimals[1].subtract(decimals[0]), is(ONE_MILLI));
  }

  private static void assertDecrease(BigDecimal[] decimals) {
    assertThat(decimals[0].subtract(decimals[1]), is(ONE_MILLI));
  }

  protected static void assertTypeCastException(ThrowingRunnable runnable, String message) {
    assertTypeCastException(runnable, message, null);
  }

  protected static void assertTypeCastException(
      ThrowingRunnable runnable, String message, Class<? extends Throwable> causeClass) {
    TypeCastException e = assertThrows(TypeCastException.class, runnable);
    assertThat(e.getMessage(), is(message));
    assertThat(e.getCause(), is(causeClass != null ? instanceOf(causeClass) : nullValue()));
  }
}
