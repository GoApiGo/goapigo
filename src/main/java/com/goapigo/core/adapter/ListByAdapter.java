package com.goapigo.core.adapter;

import com.goapigo.core.GoApiGoProcessor;
import com.goapigo.core.annotations.ListBy;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.jsoup.Jsoup;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
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
    var service = new GoApiGoProcessor();
    return elements.stream()
        .map(element -> service.processElement(element.html(), responseClass))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }
}
