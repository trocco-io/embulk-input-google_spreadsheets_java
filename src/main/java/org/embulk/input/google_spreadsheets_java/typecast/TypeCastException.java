package org.embulk.input.google_spreadsheets_java.typecast;

import org.embulk.spi.DataException;

public class TypeCastException extends DataException {
  public TypeCastException(String message) {
    super(message);
  }

  public TypeCastException(String message, Throwable cause) {
    super(message, cause);
  }
}
