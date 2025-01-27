package org.embulk.input.google_spreadsheets_java;

import static org.embulk.input.google_spreadsheets_java.SpreadsheetsUrlUtil.captureId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;

public class SpreadsheetsUrlUtilTest {
  @Test
  public void captureIdWithCorrectUrl() {
    assertThat(
        captureId(
            "https://docs.google.com/spreadsheets/d/1Cxz-LudQuhRAGZL8mBoHs6mRnpjODpyF4Rwc5UYoV1E/edit#gid=0"),
        is("1Cxz-LudQuhRAGZL8mBoHs6mRnpjODpyF4Rwc5UYoV1E"));
    assertThat(captureId("https://docs.google.com/spreadsheets/d/a/"), is("a"));
  }

  @Test
  public void captureIdWithIncorrectUrl() {
    assertThat(captureId(""), is(nullValue()));
    assertThat(captureId("https://docs.google.com/spreadsheets/d/"), is(nullValue()));
    assertThat(captureId("https://docs.google.com/spreadsheets/d//"), is(nullValue()));
  }
}
