package com.tcbs.automation.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusInfo {

  private String value;

  private String rejectReason;

  private String rejectContent;

  private String rejectPerson;

  private Date updatedDate;

}
