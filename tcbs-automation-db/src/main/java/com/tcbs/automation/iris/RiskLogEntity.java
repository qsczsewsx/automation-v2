package com.tcbs.automation.iris;

import com.tcbs.automation.config.idatatcanalysis.IData;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "RISK_MARGIN_LOG")
public class RiskLogEntity {
  @Column(name = "ID")
  private Integer id;
  @Id
  @Column(name = "TICKER")
  private String value;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "ERROR")
  private String error;

  @Column(name = "WARNING")
  private String warning;

  @Column(name = "CURRENT_PRICE")
  private Integer currentPrice;

  @Column(name = "CURRENT_RATIO")
  private Float currentRatio;

  @Column(name = "ROOM_REMAIN")
  private Integer roomRemain;

  @Column(name = "ROOM_USED")
  private Integer roomUsed;

  @Column(name = "RATIO")
  private Float ratio;

  @Column(name = "VAR")
  private Float var;

  @Column(name = "AUTO_ROOM")
  private Integer autoRoom;

  @Column(name = "DEBT")
  private Integer debt;

  @Column(name = "EXC_ROOM")
  private Integer excRoom;

  @Column(name = "FINAL_ROOM")
  private Integer finalRoom;

  @Column(name = "NOTE")
  private String note;

  @Step("get all conditions")
  public static List getById(int logId) {
    StringBuilder query = new StringBuilder();
    try {
      query.append("SELECT * FROM RISK_MARGIN_LOG WHERE ID = :logId ");
      return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("logId", logId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }
}
