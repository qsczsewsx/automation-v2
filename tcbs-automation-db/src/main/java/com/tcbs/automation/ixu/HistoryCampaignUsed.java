package com.tcbs.automation.ixu;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "HISTORY_CAMPAIGN_USED")
public class HistoryCampaignUsed {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;

  @Column(name = "AMOUNT")
  private Double amount;

  @Column(name = "PRIORITY_PROGRAM_ID")
  private Integer priorityProgramId;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "LAST_MODIFY_DATE")
  private Date lastModifyDate;

}
