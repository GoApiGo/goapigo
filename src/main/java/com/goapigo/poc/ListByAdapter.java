package com.goapigo.poc;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListByAdapter implements Adaptable<List<?>> {

    private final Annotation annotation;
    private final String htmlContent;
    private final Class<? extends Object> responseClass;

    public List<?> adapt() {
        ListBy listBy = (ListBy) annotation;
        String cssSelector = listBy.value();
        Document document = Jsoup.parse(htmlContent);
        Elements elements = document.select(cssSelector);
        return elements.stream().map(element -> RepositoryDTO.builder().name("teste").build()).collect(Collectors.toList());
    }

}
