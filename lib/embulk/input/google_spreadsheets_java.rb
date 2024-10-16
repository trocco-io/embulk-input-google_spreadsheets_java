Embulk::JavaPlugin.register_input(
  "google_spreadsheets_java", "org.embulk.input.google_spreadsheets_java.GoogleSpreadsheetsJavaInputPlugin",
  File.expand_path('../../../../classpath', __FILE__))
