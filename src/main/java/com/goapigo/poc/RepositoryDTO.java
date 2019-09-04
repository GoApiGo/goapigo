package com.goapigo.poc;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@GoApiGo
public class RepositoryDTO {

    @InnerTextBy("ul > li:nth-child(1) > div.col-10.col-lg-9.d-inline-block > div.d-inline-block.mb-1 > h3 > a")
    private String name;

    @InnerTextBy("ul > li:nth-child(1) > div.col-10.col-lg-9.d-inline-block > div:nth-child(2) > p")
    private String description;

    @AttributeBy(value = "ul > li:nth-child(1) > div.col-10.col-lg-9.d-inline-block > div.d-inline-block.mb-1 > h3 > a", attribute = "href")
    private String url;

    @InnerTextBy("ul > li:nth-child(1) > div.col-10.col-lg-9.d-inline-block > div.f6.text-gray.mt-2 > a")
    private Integer numberOfForks;

}
