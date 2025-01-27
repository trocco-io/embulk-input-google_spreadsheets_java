package org.embulk.input.google_spreadsheets_java;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.embulk.input.google_spreadsheets_java.typecast.TypeCastException;
import org.embulk.util.config.units.ColumnConfig;

public class RecordTypeCaster {
  private final List<String> columnNames;
  private final Map<String, ColumnDetail> columnDetails;

  public RecordTypeCaster(PluginTask task) {
    List<ColumnConfig> columns = task.getColumns().getColumns();
    columnNames = columns.stream().map(ColumnConfig::getName).collect(Collectors.toList());
    columnDetails =
        IntStream.range(0, columns.size())
            .mapToObj(index -> toDetail(task, columns, index))
            .collect(Collectors.toMap(detail -> detail.name, detail -> detail));
  }

  public List<Object> castByColumns(List<Object> record) {
    return columnNames.stream()
        .map(name -> castBy(columnDetails.get(name), record))
        .collect(Collectors.toList());
  }

  private static ColumnDetail toDetail(PluginTask task, List<ColumnConfig> columns, int index) {
    ColumnConfig column = columns.get(index);
    return new ColumnDetail(
        index,
        column.getName(),
        column.getType(),
        column.getOption().get(String.class, "format", task.getDefaultTimestampFormat()),
        column.getOption().get(String.class, "timezone", task.getDefaultTimezone()),
        column.getOption().get(String.class, "typecast", task.getDefaultTypecast()),
        task);
  }

  private static Object castBy(ColumnDetail detail, List<Object> record) {
    try {
      return detail.cast(record);
    } catch (TypeCastException e) {
      throw new TypeCastException(
          e.getMessage() + ", column: '" + detail.name + "', detail: " + detail, e.getCause());
    }
  }
}
