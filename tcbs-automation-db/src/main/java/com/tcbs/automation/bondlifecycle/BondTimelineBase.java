package com.tcbs.automation.bondlifecycle;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BondTimelineBase extends RuleBaseEntity {
  @Column(name = "BOND_TIMELINE_GROUP_ID")
  private Integer bondTimelineGroupId;

  public BondTimelineBase(Integer groupId, RuleBaseEntity ruleBase) {
    super(ruleBase);
    this.bondTimelineGroupId = groupId;
  }

  public Integer getBondTimelineGroupId() {
    return bondTimelineGroupId;
  }

  public void setBondTimelineGroupId(Integer bondTimelineGroupId) {
    this.bondTimelineGroupId = bondTimelineGroupId;
  }
}
