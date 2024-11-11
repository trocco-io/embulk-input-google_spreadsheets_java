package org.embulk.input.google_spreadsheets_java;

import static org.embulk.input.google_spreadsheets_java.PagerUtil.toLetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class PagerUtilTest {
  @Test
  public void toLettersWithPositiveValue() {
    assertThat(toLetters(1), is("A"));
    assertThat(toLetters(26), is("Z"));
    assertThat(toLetters(27), is("AA"));
  }

  @Test
  public void toLettersWithZeroOrNegativeValue() {
    assertThat(toLetters(0), is(""));
    assertThat(toLetters(-1), is(""));
    assertThat(toLetters(-26), is(""));
  }
}
