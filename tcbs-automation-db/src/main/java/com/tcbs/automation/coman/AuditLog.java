package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "AUDIT_LOG")
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "MODEL")
  private String model;
  @Column(name = "RESULT")
  private String result;
  @Column(name = "CREATOR")
  private String creator;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;

  @Step
  public static AuditLog getAuditLog() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * ");
    queryStringBuilder.append("FROM AUDIT_LOG  ");
    queryStringBuilder.append("ORDER BY id DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY");

    Session session = Connection.comanDbConnection.getSession();
    Object[] obj = (Object[]) session.createSQLQuery(queryStringBuilder.toString()).getSingleResult();
    AuditLog rs = new AuditLog();
    rs.setId((BigDecimal) obj[0]);
    rs.setAction((String) obj[1]);
    rs.setModel((String) obj[2]);
    rs.setCreator((String) obj[5]);
    rs.setCreatedAt((Timestamp) obj[6]);
    rs.setUpdatedAt((Timestamp) obj[7]);
    return rs;
  }
  //SELECT * from AUDIT_LOG ORDER BY id DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY ;
}
