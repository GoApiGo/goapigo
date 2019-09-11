package com.goapigo.core.adapter;

import com.goapigo.core.annotations.AttributeBy;
import com.goapigo.core.annotations.TextBy;
import com.goapigo.core.exception.ElementParsingException;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AttributeByAdapterTest {

  public static final String DIV_ID_TEMPLATE = "<div id=\"%s\"><a href=\"%s\">Some text</a><div>";
  public static final String DEFAULT_ATTRIBUTE_TEXT = "#anchor";
  public static final String ID_SOMETHING = "something";
  public static final String SOME_NUMBER_FIELD_NAME = "someNumber";
  public static final String ID_SOME_NUMBER = "some-number";

  @Test
  public void adapt() throws NoSuchFieldException {
    var somethingField = AdaptableClass.class.getDeclaredField(ID_SOMETHING);
    var annotation = somethingField.getAnnotation(AttributeBy.class);
    var htmlContent = String.format(DIV_ID_TEMPLATE, ID_SOMETHING, DEFAULT_ATTRIBUTE_TEXT);
    var adapter = new AttributeByAdapter(annotation, htmlContent, somethingField.getType());
    var response = adapter.adapt();
    assertThat(response).isNotNull().isEqualTo(DEFAULT_ATTRIBUTE_TEXT);
  }

  @Test
  public void adaptNumber() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField(SOME_NUMBER_FIELD_NAME);
    var annotation = someNumberField.getAnnotation(AttributeBy.class);
    var htmlContent = String.format(DIV_ID_TEMPLATE, ID_SOME_NUMBER, "1");
    var adapter = new AttributeByAdapter(annotation, htmlContent, someNumberField.getType());
    var response = adapter.adapt();
    assertThat(response).isNotNull().isInstanceOf(Integer.class).isEqualTo(1);
  }

  @Test(expected = ElementParsingException.class)
  public void adaptNumberWhenIsNotANumber() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField(SOME_NUMBER_FIELD_NAME);
    var annotation = someNumberField.getAnnotation(AttributeBy.class);
    var htmlContent = String.format(DIV_ID_TEMPLATE, ID_SOME_NUMBER, "XYZ");
    var adapter = new AttributeByAdapter(annotation, htmlContent, someNumberField.getType());
    adapter.adapt();
  }

  @Test
  public void adaptWhenNotParseable() throws NoSuchFieldException {
    Field someNumberField = AdaptableClass.class.getDeclaredField(SOME_NUMBER_FIELD_NAME);
    var annotation = someNumberField.getAnnotation(AttributeBy.class);
    var htmlContent = String.format(DIV_ID_TEMPLATE, ID_SOME_NUMBER, "XYZ");
    var adapter = new AttributeByAdapter(annotation, htmlContent, getClass());
    var response = adapter.adapt();
    assertThat(response).isNull();
  }

  private class AdaptableClass {
    @AttributeBy(value = "#something a", attribute = "href")
    private String something;

    @AttributeBy(value = "#some-number a", attribute = "href")
    private Integer someNumber;
  }
}
