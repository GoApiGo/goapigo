package com.goapigo.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.goapigo.core.dto.AdaptableClass;
import com.goapigo.core.dto.AdaptableElementClass;
import com.goapigo.core.dto.NotAdaptableClass;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GoApiGoProcessorTest {

  private static final String HTML_CONTENT =
      "<ul id=\"some-list\">"
          + "<li>First element</li>"
          + "<li>Second element</li>"
          + "<li>Third element</li>"
          + "<ul>"
          + "<ul id=\"another-list\">"
          + "<li><span>1</span><span>Element 1</span></li>"
          + "<li><span>2</span><span>Element 2</span></li>"
          + "<li><span>3</span><span>Element 3</span></li>"
          + "</ul>";
  @InjectMocks private GoApiGoProcessor processor;

  @Test
  public void go() {
    AdaptableClass adaptableClass = processor.go(HTML_CONTENT, AdaptableClass.class);
    assertThat(adaptableClass).isNotNull();
    assertThat(adaptableClass.getElements()).isNotNull().hasSize(3);
    assertThat(adaptableClass.getAnotherElements()).isNotNull().hasSize(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void goWithEntityNotAnnotatedByGoApiGo() {
    processor.go(HTML_CONTENT, AdaptableElementClass.class);
  }

  @Test(expected = NoSuchElementException.class)
  public void goWithError() {
    processor.go(HTML_CONTENT, NotAdaptableClass.class);
  }
}
