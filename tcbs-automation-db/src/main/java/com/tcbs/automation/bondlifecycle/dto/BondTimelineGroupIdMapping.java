package com.tcbs.automation.bondlifecycle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class BondTimelineGroupIdMapping {

  private Integer id;

  public BondTimelineGroupIdMapping(Integer id) {
    super();
    this.id = id;
  }
}
