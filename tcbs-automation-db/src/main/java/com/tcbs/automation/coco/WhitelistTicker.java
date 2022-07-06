package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.coco.CocoConnBridge.socialInvestConnection;

@Entity
@Getter
@Setter
@Table(name = "WHITELIST_TICKER")
public class WhitelistTicker {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "ACTIVE_DATE")
  private Date activeDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "INACTIVE_DATE")
  private Date inactiveDate;


  public List<String> getAllWhitelistTicker() {
    Query query = socialInvestConnection.getSession()
      .createNativeQuery("select ticker from WHITELIST_TICKER where status = 'ACTIVE'");
    List<String> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
