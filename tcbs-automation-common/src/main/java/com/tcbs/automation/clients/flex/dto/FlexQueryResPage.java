package com.tcbs.automation.clients.flex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FlexQueryResPage<T> {
  private Integer totalCount;
  private Integer pageSize;
  private Integer pageIndex;
  private List<T> data;
  private String object;
}
