package com.goapigo.poc.adapter;

import com.goapigo.poc.annotations.GoApiGo;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class GoApiGoService {

  public <T extends Object> T process(String htmlContent, Class<T> entity) {
    if (!entity.isAnnotationPresent(GoApiGo.class)) {
      throw new IllegalArgumentException(
          String.format("Entity %s class must be annotated by @GoApiGo", entity.getSimpleName()));
    }
    return processElement(htmlContent, entity).get();
  }

  <T extends Object> Optional<T> processElement(String htmlContent, Class<T> entity) {
    try {
      T entityInstance = entity.getConstructor().newInstance();
      Reflections reflections = new Reflections("com.goapigo.poc.annotations");
      reflections.getSubTypesOf(Annotation.class).stream()
          .filter(annotation -> SelectorAnnotations.getByAnnotationClass(annotation) != null)
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

  private <T extends Object> void processField(
      String htmlContent, T entityInstance, SelectorAnnotations selector, Field field) {
    Class<? extends Adaptable> adaptable = selector.getAdapterClass();
    try {
      Adaptable<?> instance =
          adaptable
              .getConstructor(Annotation.class, String.class, Class.class)
              .newInstance(
                  field.getAnnotation(selector.getAnnotationClass()), htmlContent, getType(field));
      Object adapted = adaptable.getMethod("adapt").invoke(instance);
      field.setAccessible(true);
      field.set(entityInstance, adapted);
    } catch (Exception e) {
      log.error("Error parsing html.", e);
    }
  }

  private Class<?> getType(Field field) {
    if (!(field.getGenericType() instanceof ParameterizedType)) {
      return field.getType();
    }
    ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
    return (Class<?>) stringListType.getActualTypeArguments()[0];
  }
}
