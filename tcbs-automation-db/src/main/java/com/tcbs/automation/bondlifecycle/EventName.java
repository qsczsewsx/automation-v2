package com.tcbs.automation.bondlifecycle;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "EVENT_NAME")
@Data
@EqualsAndHashCode
public class EventName extends ApprovalEntity {
  @Id
  @Column(name = "EVENT_NAME_ID", updatable = false, nullable = false)
  private Integer eventNameId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NAME_NO_ACCENT")
  private String nameNoAccent;

  @Column(name = "NEW_NAME")
  private String draftName;

  @Column(name = "EVENT_TYPE_ID")
  private Integer eventTypeId;
}
