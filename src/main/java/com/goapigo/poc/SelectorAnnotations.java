package com.goapigo.poc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum SelectorAnnotations {

    ATTRIBUTE(AttributeBy.class, null), INNERTEXT(InnerTextBy.class, null), LIST(ListBy.class, ListByAdapter.class);

    private Class<? extends Annotation> annotationClass;
    private Class<? extends Adaptable> adapterClass;

    public static SelectorAnnotations getByAnnotationClass(Class<? extends Annotation> clazz) {
        return Arrays.stream(SelectorAnnotations.values()).filter(selector -> selector.annotationClass.equals(clazz)).findFirst().orElse(null);
    }

}
