package com.goapigo.core;

import com.goapigo.core.adapter.Adaptable;
import com.goapigo.core.adapter.SelectorAnnotations;
import com.goapigo.core.annotations.GoApiGo;
import com.goapigo.core.exception.ElementParsingException;
import com.goapigo.core.util.AdapterUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.reflections.Reflections;

@Slf4j
public class GoApiGoProcessor {

  public <T extends Object> T go(String htmlContent, Class<T> entity) {
    if (!entity.isAnnotationPresent(GoApiGo.class)) {
      throw new IllegalArgumentException(
          String.format("Entity %s class must be annotated by @GoApiGo", entity.getSimpleName()));
    }
    return processElement(htmlContent, entity).get();
  }

  public <T extends Object> Optional<T> processElement(String htmlContent, Class<T> entity) {
    try {
      T entityInstance = entity.getConstructor().newInstance();
      Reflections reflections = new Reflections("com.goapigo.core.annotations");
      reflections.getSubTypesOf(Annotation.class).stream()
          .filter(this::isAnnotated)
          .forEach(
              annotation -> {
                var selector = SelectorAnnotations.getByAnnotationClass(annotation);
                Arrays.stream(entity.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(selector.getAnnotationClass()))
                    .forEach(field -> processField(htmlContent, entityInstance, selector, field));
              });
      return Optional.of(entityInstance);
    } catch (Exception e) {
      log.error("Error parsing html.", e);
      return Optional.empty();
    }
  }

  private boolean isAnnotated(Class<? extends Annotation> annotation) {
    return SelectorAnnotations.getByAnnotationClass(annotation) != null;
  }

  private <T extends Object> void processField(
      String htmlContent, T entityInstance, SelectorAnnotations selector, Field field) {
    Class<? extends Adaptable> adaptable = selector.getAdapterClass();
    try {
      Adaptable<?> instance =
          adaptable
              .getConstructor(Annotation.class, String.class, Class.class)
              .newInstance(
                  field.getAnnotation(selector.getAnnotationClass()),
                  htmlContent,
                  AdapterUtil.getType(field));
      Object adapted = adaptable.getMethod("adapt").invoke(instance);
      field.setAccessible(true);
      field.set(entityInstance, adapted);
    } catch (Exception e) {
      log.error("Error parsing html.", e);
      throw new ElementParsingException(e);
    }
  }
}
