package com.goapigo.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ElementParsingException extends RuntimeException {

  public ElementParsingException(Exception e) {
    super(e);
  }
}
