package com.tcbs.automation.coco.mongodb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortItem {
  private String key;
  @JsonProperty("isAsc")
  private boolean isAsc;
}
