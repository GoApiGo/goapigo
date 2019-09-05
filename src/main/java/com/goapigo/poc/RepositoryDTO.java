package com.goapigo.poc;

import com.goapigo.poc.annotations.GoApiGo;
import com.goapigo.poc.annotations.TextBy;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@GoApiGo
public class RepositoryDTO {

  @TextBy("div.col-10.col-lg-9.d-inline-block > div.d-inline-block.mb-1 > h3 > a")
  private String name;

  @TextBy("div.col-10.col-lg-9.d-inline-block > div:nth-child(2) > p")
  private String description;

  //    @AttributeBy(value = "ul > li:nth-child(1) > div.col-10.col-lg-9.d-inline-block >
  // div.d-inline-block.mb-1 > h3 > a", attribute = "href")
  //    private String url;

  @TextBy("div.col-10.col-lg-9.d-inline-block > div.f6.text-gray.mt-2 > a")
  private Integer numberOfForks;
}
