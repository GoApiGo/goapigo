package com.goapigo.poc.adapter;

import com.goapigo.poc.annotations.TextBy;
import com.goapigo.poc.exception.ElementParsingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.annotation.Annotation;

@Slf4j
@RequiredArgsConstructor
public class TextByAdapter implements Adaptable<Object> {

  private final Annotation annotation;
  private final String htmlContent;
  private final Class<? extends Object> responseClass;

  public Object adapt() {
    TextBy textBy = (TextBy) annotation;
    String cssSelector = textBy.value();
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
      if (responseClass.getConstructor(String.class) != null) {
        return responseClass.getConstructor(String.class).newInstance(element.text());
      }
      return element.text();
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
