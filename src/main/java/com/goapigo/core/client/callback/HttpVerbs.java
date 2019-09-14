package com.goapigo.core.client.callback;

import com.goapigo.core.client.annotations.GoGet;
import java.lang.annotation.Annotation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum HttpVerbs {
  GET(GoGet.class);

  private Class<? extends Annotation> annotationClass;
}
