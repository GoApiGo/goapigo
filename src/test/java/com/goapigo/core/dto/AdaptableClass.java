package com.goapigo.core.dto;

import com.goapigo.core.annotations.GoApiGo;
import com.goapigo.core.annotations.ListBy;
import java.util.List;
import lombok.Getter;

@GoApiGo
@Getter
public class AdaptableClass {
  @ListBy("#some-list > li")
  private List<String> elements;

  @ListBy("#another-list > li")
  private List<AdaptableElementClass> anotherElements;
}
