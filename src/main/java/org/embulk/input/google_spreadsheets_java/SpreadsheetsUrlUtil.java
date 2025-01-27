package org.embulk.input.google_spreadsheets_java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpreadsheetsUrlUtil {
  private static final String BASE_URL = "https://docs.google.com/spreadsheets/d/";
  private static final Pattern CAPTURE_ID_PATTERN =
      Pattern.compile(String.format("%s([^/]+).*", Pattern.quote(BASE_URL)));

  // https://developers.google.com/sheets/api/guides/concepts#spreadsheet-id
  public static String captureId(String url) {
    Matcher matcher = CAPTURE_ID_PATTERN.matcher(url);
    return matcher.matches() ? matcher.group(1) : null;
  }
}
