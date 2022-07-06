package com.tcbs.automation.ixu;

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
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "NOTIFY_CONTENT")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotifyContent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "GENERAL_TRANSACTION_ID")
  private String generalTransactionId;
  @Column(name = "CONTENT")
  private String content;
  @Column(name = "TITLE")
  private String title;
  @Column(name = "BRIEF")
  private String brief;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED")
  private Timestamp lastUpdated;

  @Step
  public static List<NotifyContent> findByGeneralTransactionId(String generalTransactionId) {
    ixuDbConnection.getSession().clear();
    Query<NotifyContent> query = ixuDbConnection.getSession().createQuery(
      "from NotifyContent a where a.generalTransactionId=:generalTransactionId");
    query.setParameter("generalTransactionId", generalTransactionId);
    List<NotifyContent> notifyContentList = query.getResultList();
    ixuDbConnection.closeSession();
    return notifyContentList;
  }
}
