package com.tcbs.automation.tca.activitynews;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stx_msg_newsEN")
public class ActivityNewsEnInfo {
  @Column(name = "newsID")
  @Id
  private Long id;
  @Column(name = "OrganCode")
  private String ticker;
  @Column(name = "newsTitle")
  private String msgTitle;
  @Column(name = "PublicDate")
  private Date msgPostDate;
  @Column(name = "Status")
  private Integer msgStatus;

}

