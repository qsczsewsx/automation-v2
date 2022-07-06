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
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "MESSAGE_HISTORY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageHistory {

  private static final String SOURCE = "source";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "CREATED_DATE")
  private Date createdDate = new Date();

  @Step
  public static void deleteBySource(String source) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from MessageHistory a where a.source=:source");
    query.setParameter(SOURCE, source);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<MessageHistory> findBySource(String source) {
    ixuDbConnection.getSession().clear();
    Query<MessageHistory> query = ixuDbConnection.getSession().createQuery(
      "from MessageHistory a where a.source=:source");
    query.setParameter(SOURCE, source);
    List<MessageHistory> messageHistoryList = query.getResultList();
    ixuDbConnection.closeSession();
    return messageHistoryList;
  }

  @Step
  public static void insert(MessageHistory messageHistory) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(messageHistory);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();

  }

  @Step
  public static void deleteBySourceAndCreateDate(String source, String date) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from MessageHistory a where a.source=:source and trunc(a.createdDate) = to_date(:date, 'YYYY-MM-DD')");
    query.setParameter(SOURCE, source);
    query.setParameter("date", date);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
