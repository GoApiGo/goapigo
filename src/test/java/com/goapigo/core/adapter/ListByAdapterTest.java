package com.goapigo.core.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.goapigo.core.annotations.ListBy;
import com.goapigo.core.dto.AdaptableClass;
import com.goapigo.core.dto.AdaptableElementClass;
import com.goapigo.core.util.AdapterUtil;
import java.util.List;
import java.util.Optional;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListByAdapterTest {

  private static final String LIST_ELEMENTS_TEMPLATE =
      "<ul id=\"some-list\">"
          + "<li>First element</li>"
          + "<li>Second element</li>"
          + "<li>Third element</li><ul>";
  private static final String LIST_ANOTHER_ELEMENTS_TEMPLATE =
      "<ul id=\"another-list\">"
          + "<li><span>1</span><span>Element 1</span></li>"
          + "<li><span>2</span><span>Element 2</span></li>"
          + "<li><span>3</span><span>Element 3</span></li>"
          + "</ul>";
  private static final String ELEMENTS_FIELD_NAME = "elements";
  private static final String ANOTHER_ELEMENTS_FIELD_NAME = "anotherElements";

  @Test
  public void adaptStringList() throws NoSuchFieldException {
    var elementsField = AdaptableClass.class.getDeclaredField(ELEMENTS_FIELD_NAME);
    var annotation = elementsField.getAnnotation(ListBy.class);
    var adapter =
        new ListByAdapter(annotation, LIST_ELEMENTS_TEMPLATE, AdapterUtil.getType(elementsField));
    var response = (List<String>) adapter.adapt();
    assertThat(response).isNotNull().hasSize(3);
    assertSoftly(
        softAssertions ->
            response.forEach(element -> softAssertions.assertThat(element).isNotNull()));
  }

  @Test
  public void adaptObjectList() throws NoSuchFieldException {
    var anotherElementsField = AdaptableClass.class.getDeclaredField(ANOTHER_ELEMENTS_FIELD_NAME);
    var annotation = anotherElementsField.getAnnotation(ListBy.class);
    var adapter =
        new ListByAdapter(
            annotation, LIST_ANOTHER_ELEMENTS_TEMPLATE, AdapterUtil.getType(anotherElementsField));
    var response = (List<AdaptableElementClass>) adapter.adapt();
    assertThat(response).isNotNull().hasSize(3);
    assertSoftly(
        softAssertions ->
            response.forEach(element -> softAssertions.assertThat(element).isNotNull()));
  }
}
