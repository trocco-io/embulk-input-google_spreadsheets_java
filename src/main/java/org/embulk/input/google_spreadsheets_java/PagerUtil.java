package org.embulk.input.google_spreadsheets_java;

public class PagerUtil {
  private static final int BASE = 26;
  private static final char OFFSET = 'A';

  // https://developers.google.com/sheets/api/guides/concepts#cell
  // https://en.wikipedia.org/wiki/Bijective_numeration#The_bijective_base-26_system
  public static String toLetters(int number) {
    StringBuilder builder = new StringBuilder();
    for (int i = number; i > 0; i /= BASE) {
      builder.append((char) (--i % BASE + OFFSET));
    }
    return builder.reverse().toString();
  }
}
