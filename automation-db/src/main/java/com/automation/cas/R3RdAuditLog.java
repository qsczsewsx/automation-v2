package com.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "R3RD_AUDIT_LOG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R3RdAuditLog {
  private static Logger logger = LoggerFactory.getLogger(R3RdAuditLog.class);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "xxxx_ID")
  private String xxxxId;
  @Column(name = "USERNAME")
  private String username;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "DATA")
  private String data;
  @Column(name = "CREATE_BY_USER")
  private String createByUser;

  @Step
  public static List<R3RdAuditLog> getHistoryByxxxxId(String xxxxId) {
    CAS.casConnection.getSession().clear();
    Query<R3RdAuditLog> query = CAS.casConnection.getSession().createQuery(
      "from R3RdAuditLog a where a.xxxxId =: xxxxId order by createdDate desc", R3RdAuditLog.class);
    query.setParameter("xxxxId", xxxxId);
    return query.getResultList();
  }

}