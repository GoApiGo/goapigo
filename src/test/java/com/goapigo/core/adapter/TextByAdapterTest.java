package com.goapigo.core.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.goapigo.core.annotations.TextBy;
import com.goapigo.core.exception.ElementParsingException;
import java.lang.reflect.Field;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TextByAdapterTest {

  public static final String DIV_ID_TEMPLATE = "<div id=\"%s\">%s<div>";
  public static final String DEFAULT_TEXT = "My Text";
  public static final String ID_SOMETHING = "something";
  public static final String SOME_NUMBER_FIELD_NAME = "someNumber";
  public static final String ID_SOME_NUMBER = "some-number";

  @Test
  public void adapt() throws NoSuchFieldException {
    var somethingField = AdaptableClass.class.getDeclaredField(ID_SOMETHING);
    var annotation = somethingField.getAnnotation(TextBy.class);
    var htmlContent = String.format(DIV_ID_TEMPLATE, ID_SOMETHING, DEFAULT_TEXT);
    var adapter = new TextByAdapter(annotation, htmlContent, somethingField.getType());
    var response = adapter.adapt();
    assertThat(response).isNotNull().isEqualTo(DEFAULT_TEXT);
  }

  @Test
  public void adaptNumber() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField(SOME_NUMBER_FIELD_NAME);
    var annotation = someNumberField.getAnnotation(TextBy.class);
    var htmlContent = String.format(DIV_ID_TEMPLATE, ID_SOME_NUMBER, "1");
    var adapter = new TextByAdapter(annotation, htmlContent, someNumberField.getType());
    var response = adapter.adapt();
    assertThat(response).isNotNull().isInstanceOf(Integer.class).isEqualTo(1);
  }

  @Test(expected = ElementParsingException.class)
  public void adaptNumberWhenIsNotANumber() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField(SOME_NUMBER_FIELD_NAME);
    var annotation = someNumberField.getAnnotation(TextBy.class);
    var htmlContent = String.format(DIV_ID_TEMPLATE, ID_SOME_NUMBER, "XYZ");
    var adapter = new TextByAdapter(annotation, htmlContent, someNumberField.getType());
    adapter.adapt();
  }

  @Test
  public void adaptWhenNotParseable() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField(SOME_NUMBER_FIELD_NAME);
    var annotation = someNumberField.getAnnotation(TextBy.class);
    var htmlContent = String.format(DIV_ID_TEMPLATE, ID_SOME_NUMBER, "XYZ");
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
