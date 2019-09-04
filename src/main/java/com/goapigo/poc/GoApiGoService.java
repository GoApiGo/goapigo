package com.goapigo.poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@Slf4j
@Service
public class GoApiGoService {

    public <T extends Object> T process(String htmlContent, Class<T> entity) {
        if (!entity.isAnnotationPresent(GoApiGo.class)) {
            throw new IllegalArgumentException(String.format("Entity %s class must be annotated by @GoApiGo", entity.getSimpleName()));
        }
        try {
            T entityInstance = entity.getConstructor().newInstance();

            Arrays.stream(SelectorAnnotations.values()).forEach(selector -> {
                Arrays.stream(entity.getDeclaredFields()).filter(field -> field.isAnnotationPresent(selector.getAnnotationClass())).forEach(field -> {
                    Class<? extends Adaptable> adaptable = selector.getAdapterClass();
                    try {
                        Adaptable<?> instance = adaptable.getConstructor(Annotation.class, String.class, Class.class).newInstance(field.getAnnotation(selector.getAnnotationClass()), htmlContent, field.getType());
                        Object adapted = adaptable.getMethod("adapt").invoke(instance);
                        field.setAccessible(true);
                        field.set(entityInstance, adapted);
                    } catch (Exception e) {
                        log.error("Error parsing html.", e);
                    }
                });
            });
            return entityInstance;
        } catch (Exception e) {
            log.error("Error parsing html.", e);
        }
        return null;
    }

}
