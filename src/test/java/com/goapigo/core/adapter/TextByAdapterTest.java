package com.goapigo.core.adapter;

import com.goapigo.core.annotations.TextBy;
import com.goapigo.core.exception.ElementParsingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TextByAdapterTest {

  @Test
  public void adapt() throws NoSuchFieldException {
    var somethingField = AdaptableClass.class.getDeclaredField("something");
    var annotation = somethingField.getAnnotation(TextBy.class);
    var htmlContent = "<div id=\"something\">My Text<div>";
    var adapter = new TextByAdapter(annotation, htmlContent, somethingField.getType());
    var response = adapter.adapt();
    assertThat(response).isNotNull().isEqualTo("My Text");
  }

  @Test
  public void adaptNumber() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField("someNumber");
    var annotation = someNumberField.getAnnotation(TextBy.class);
    var htmlContent = "<div id=\"some-number\">1<div>";
    var adapter = new TextByAdapter(annotation, htmlContent, someNumberField.getType());
    var response = adapter.adapt();
    assertThat(response).isNotNull().isInstanceOf(Integer.class).isEqualTo(1);
  }

  @Test(expected = ElementParsingException.class)
  public void adaptNumberWhenIsNotANumber() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField("someNumber");
    var annotation = someNumberField.getAnnotation(TextBy.class);
    var htmlContent = "<div id=\"some-number\">XYZ<div>";
    var adapter = new TextByAdapter(annotation, htmlContent, someNumberField.getType());
    adapter.adapt();
  }

  @Test
  public void adaptWhenNotParseable() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField("someNumber");
    var annotation = someNumberField.getAnnotation(TextBy.class);
    var htmlContent = "<div id=\"some-number\">XYZ<div>";
    var adapter = new TextByAdapter(annotation, htmlContent, getClass());
    var response = adapter.adapt();
    assertThat(response).isNull();
  }

  private class AdaptableClass {
    @TextBy("#something")
    private String something;

    @TextBy("#some-number")
    private Integer someNumber;
  }
}
