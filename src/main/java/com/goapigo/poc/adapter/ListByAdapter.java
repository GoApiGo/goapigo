package com.goapigo.poc.adapter;

import com.goapigo.poc.annotations.ListBy;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListByAdapter implements Adaptable<List<?>> {

  private final Annotation annotation;
  private final String htmlContent;
  private final Class<? extends Object> responseClass;

  public List<?> adapt() {
    var listBy = (ListBy) annotation;
    var cssSelector = listBy.value();
    var document = Jsoup.parse(htmlContent);
    var elements = document.select(cssSelector);
    var service = new GoApiGoService();
    return elements.stream()
        .map(element -> service.processElement(element.html(), responseClass))
        .collect(Collectors.toList());
  }
}
