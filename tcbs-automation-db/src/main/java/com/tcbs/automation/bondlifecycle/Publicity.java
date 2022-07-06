package com.tcbs.automation.bondlifecycle;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PUBLICITY")
@Data
public class Publicity extends ApprovalEntity implements Serializable {
  @Id
  @Column(name = "PUBLICITY_ID", updatable = false, nullable = false)
  private Integer publicityId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NAME_NO_ACCENT")
  private String nameNoAccent;

  @Column(name = "NEW_NAME")
  private String draftName;
}