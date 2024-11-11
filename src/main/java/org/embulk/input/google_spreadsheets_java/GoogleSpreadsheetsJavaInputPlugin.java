package org.embulk.input.google_spreadsheets_java;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigException;
import org.embulk.config.ConfigSource;
import org.embulk.config.TaskReport;
import org.embulk.config.TaskSource;
import org.embulk.spi.DataException;
import org.embulk.spi.Exec;
import org.embulk.spi.InputPlugin;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.Schema;
import org.embulk.util.config.ConfigMapper;
import org.embulk.util.config.ConfigMapperFactory;
import org.embulk.util.config.TaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class GoogleSpreadsheetsJavaInputPlugin implements InputPlugin {
  private static final ConfigMapperFactory CONFIG_MAPPER_FACTORY =
      ConfigMapperFactory.builder().addDefaultModules().build();
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public ConfigDiff transaction(ConfigSource config, InputPlugin.Control control) {
    ConfigMapper configMapper = CONFIG_MAPPER_FACTORY.createConfigMapper();
    PluginTask task = configMapper.map(config, PluginTask.class);
    task.setNow(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    Schema schema = task.getColumns().toSchema();
    int taskCount = 1; // number of run() method calls
    return resume(task.toTaskSource(), schema, taskCount, control);
  }

  @Override
  public ConfigDiff resume(
      TaskSource taskSource, Schema schema, int taskCount, InputPlugin.Control control) {
    control.run(taskSource, schema, taskCount);
    return CONFIG_MAPPER_FACTORY.newConfigDiff();
  }

  @Override
  public void cleanup(
      TaskSource taskSource, Schema schema, int taskCount, List<TaskReport> successTaskReports) {}

  @Override
  @SuppressWarnings("deprecation") // TODO: For compatibility with Embulk v0.9
  public TaskReport run(TaskSource taskSource, Schema schema, int taskIndex, PageOutput output) {
    TaskMapper taskMapper = CONFIG_MAPPER_FACTORY.createTaskMapper();
    PluginTask task = taskMapper.map(taskSource, PluginTask.class);
    SpreadsheetsClient client = new SpreadsheetsClient(task, new Auth(task), new Pager(task));
    RecordTypeCaster typeCaster = new RecordTypeCaster(task);
    try (PageBuilder builder = new PageBuilder(Exec.getBufferAllocator(), schema, output)) {
      client.worksheetEachRecord(record -> add(task, typeCaster, builder, record));
      builder.finish();
    } catch (Exception e) {
      LOGGER.error("Error {} occurred", e, e);
      throw e;
    }
    return CONFIG_MAPPER_FACTORY.newTaskReport();
  }

  @Override
  public ConfigDiff guess(ConfigSource config) {
    return CONFIG_MAPPER_FACTORY.newConfigDiff();
  }

  private static void add(
      PluginTask task, RecordTypeCaster typeCaster, PageBuilder builder, List<Object> record) {
    try {
      builder
          .getSchema()
          .visitColumns(new ColumnVisitor(builder, typeCaster.castByColumns(record)));
      builder.addRecord();
    } catch (Exception e) {
      throwIfNeeded(task, e);
      LOGGER.warn("Error {} occurred. Skip {}", e, Arrays.toString(record.toArray()));
    }
  }

  private static void throwIfNeeded(PluginTask task, Exception e) {
    if (!task.getStopOnInvalidRecord()) {
      return;
    }
    if (e instanceof ConfigException) {
      throw (ConfigException) e;
    } else if (e instanceof DataException) {
      throw (DataException) e;
    } else {
      throw new DataException(e);
    }
  }
}
