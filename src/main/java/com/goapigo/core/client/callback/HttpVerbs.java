package com.goapigo.core.client.callback;

import com.goapigo.core.client.annotations.GoGet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;

@Getter
@AllArgsConstructor
enum HttpVerbs {
  GET(GoGet.class);

  private Class<? extends Annotation> annotationClass;
}
