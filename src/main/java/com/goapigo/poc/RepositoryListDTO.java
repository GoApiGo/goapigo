package com.goapigo.poc;

import com.goapigo.poc.annotations.GoApiGo;
import com.goapigo.poc.annotations.ListBy;
import lombok.Getter;

import java.util.List;

@Getter
@GoApiGo
public class RepositoryListDTO {

  @ListBy("#user-repositories-list > ul > li")
  private List<RepositoryDTO> repositories;
}
