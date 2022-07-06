package com.tcbs.automation.ixu;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "RB_HISTORY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RbHistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "RULEOPSID")
  private String ruleopsid;
  @Column(name = "RULEINPUT")
  private String ruleinput;
  @Column(name = "RULEOUTPUT")
  private String ruleoutput;
  @Column(name = "RAWDATA")
  private String rawdata;
  @NotNull
  @Column(name = "CREATEDDATE")
  private Timestamp createddate;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "RANKINGGLID")
  private String rankingglid;
  @Column(name = "REDEEMABLEGLID")
  private String redeemableglid;
  @Column(name = "KEY")
  private String key;
  @Column(name = "SEARCHKEY")
  private String searchkey;
  @Column(name = "ISSUEDDATE")
  private Timestamp issueddate;
  @NotNull
  @Column(name = "STATUS")
  private String status;
  @Column(name = "REDEEMABLEPOINT")
  private String redeemablepoint;
  @Column(name = "ROUNDREDEEMABLEPOINT")
  private String roundredeemablepoint;
  @Column(name = "RANKINGPOINT")
  private String rankingpoint;
  @Column(name = "ROUNDRANKINGPOINT")
  private String roundrankingpoint;
  @Column(name = "GETRAWDATATIME")
  private Timestamp getrawdatatime;
  @Column(name = "GETPARAMSTIME")
  private Timestamp getparamstime;
  @Column(name = "GETPRICETIME")
  private Timestamp getpricetime;
  @Column(name = "UPDATEGLTIME")
  private Timestamp updategltime;
  @Column(name = "ERRORMESSAGE")
  private String errormessage;

  @Step
  public static void clearRbHistory(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from RbHistoryEntity his where his.tcbsid=:tcbsid");
    query.setParameter("tcbsid", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
