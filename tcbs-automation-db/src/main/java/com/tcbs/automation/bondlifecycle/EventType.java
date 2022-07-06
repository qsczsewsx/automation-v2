package com.tcbs.automation.bondlifecycle;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "EVENT_TYPE")
@Data
public class EventType extends ApprovalEntity {
  @Id
  @Column(name = "EVENT_TYPE_ID", updatable = false, nullable = false)
  private Integer eventTypeId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NAME_NO_ACCENT")
  private String nameNoAccent;

  @Column(name = "NEW_NAME")
  private String draftName;
}
