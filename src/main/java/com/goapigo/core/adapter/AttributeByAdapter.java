package com.goapigo.core.adapter;

import com.goapigo.core.annotations.AttributeBy;
import com.goapigo.core.exception.ElementParsingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.annotation.Annotation;

@Slf4j
@RequiredArgsConstructor
public class AttributeByAdapter implements Adaptable<Object> {

  private final Annotation annotation;
  private final String htmlContent;
  private final Class<? extends Object> responseClass;
  private String attributeName;

  public Object adapt() {
    AttributeBy attributeBy = (AttributeBy) annotation;
    String cssSelector = attributeBy.value();
    this.attributeName = attributeBy.attribute();
    Document document = Jsoup.parse(htmlContent);
    Elements elements = document.select(cssSelector);
    return elements.stream()
        .filter(this::isParseable)
        .map(this::elementToObject)
        .findFirst()
        .orElse(null);
  }

  private Object elementToObject(Element element) {
    try {
      if (isResponseString() || responseClass.getConstructor(String.class) == null) {
        return element.attr(this.attributeName);
      }
      return responseClass
          .getConstructor(String.class)
          .newInstance(element.attr(this.attributeName));
    } catch (Exception e) {
      throw new ElementParsingException(e);
    }
  }

  private boolean isResponseString() {
    return responseClass.isAssignableFrom(String.class);
  }

  private boolean isParseable(Element element) {
    try {
      return responseClass.getConstructor(String.class) != null || isResponseString();
    } catch (NoSuchMethodException e) {
      log.error("No constructor found.", e);
      return false;
    }
  }
}
