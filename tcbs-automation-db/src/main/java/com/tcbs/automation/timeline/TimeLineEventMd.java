package com.tcbs.automation.timeline;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class TimeLineEventMd {
  private String id;
  private String defId;
  private Date dueDate;
  private String status;
  private String rootId;
  private Integer index;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public TimeLineEventMd() {
  }
}
