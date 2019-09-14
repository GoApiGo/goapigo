package com.goapigo.core.adapter;

import com.goapigo.core.annotations.AttributeBy;
import com.goapigo.core.annotations.ListBy;
import com.goapigo.core.annotations.TextBy;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SelectorAnnotations {
  INNERTEXT(TextBy.class, TextByAdapter.class),
  ATTRIBUTETEXT(AttributeBy.class, AttributeByAdapter.class),
  LIST(ListBy.class, ListByAdapter.class);

  private Class<? extends Annotation> annotationClass;
  private Class<? extends Adaptable> adapterClass;

  public static SelectorAnnotations getByAnnotationClass(Class<? extends Annotation> clazz) {
    return Arrays.stream(SelectorAnnotations.values())
        .filter(selector -> selector.annotationClass.equals(clazz))
        .findFirst()
        .orElse(null);
  }
}
