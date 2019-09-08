package com.goapigo.core.dto;

import com.goapigo.core.annotations.TextBy;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AdaptableElementClass {
  @TextBy("span:nth-child(1)")
  private Integer id;

  @TextBy("span:nth-child(2)")
  private String name;
}
