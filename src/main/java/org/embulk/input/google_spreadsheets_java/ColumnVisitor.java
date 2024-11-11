package org.embulk.input.google_spreadsheets_java;

import java.time.Instant;
import java.util.List;
import org.embulk.spi.Column;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.json.JsonValue;

public class ColumnVisitor implements org.embulk.spi.ColumnVisitor {
  private final PageBuilder builder;
  private final List<Object> record;

  public ColumnVisitor(PageBuilder builder, List<Object> record) {
    this.builder = builder;
    this.record = record;
  }

  @Override
  public void booleanColumn(Column column) {
    Boolean value = (Boolean) record.get(column.getIndex());
    if (value != null) {
      builder.setBoolean(column, value);
    } else {
      builder.setNull(column);
    }
  }

  @Override
  public void longColumn(Column column) {
    Long value = (Long) record.get(column.getIndex());
    if (value != null) {
      builder.setLong(column, value);
    } else {
      builder.setNull(column);
    }
  }

  @Override
  public void doubleColumn(Column column) {
    Double value = (Double) record.get(column.getIndex());
    if (value != null) {
      builder.setDouble(column, value);
    } else {
      builder.setNull(column);
    }
  }

  @Override
  public void stringColumn(Column column) {
    String value = (String) record.get(column.getIndex());
    if (value != null) {
      builder.setString(column, value);
    } else {
      builder.setNull(column);
    }
  }

  @Override
  @SuppressWarnings("deprecation") // TODO: For compatibility with Embulk v0.9
  public void timestampColumn(Column column) {
    Instant value = (Instant) record.get(column.getIndex());
    if (value != null) {
      builder.setTimestamp(column, org.embulk.spi.time.Timestamp.ofInstant(value));
    } else {
      builder.setNull(column);
    }
  }

  @Override
  public void jsonColumn(Column column) {
    JsonValue value = (JsonValue) record.get(column.getIndex());
    if (value != null) {
      builder.setJson(column, value);
    } else {
      builder.setNull(column);
    }
  }
}
