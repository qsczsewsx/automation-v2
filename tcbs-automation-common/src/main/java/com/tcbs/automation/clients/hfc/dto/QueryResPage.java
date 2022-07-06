package com.tcbs.automation.clients.hfc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
public class QueryResPage<T> {
  @Builder.Default
  private String object = "list";
  private Integer totalCount;
  private Integer pageSize;
  private Integer pageIndex;
  private List<T> data;

  public QueryResPage() {
    // default constructor
  }
}
