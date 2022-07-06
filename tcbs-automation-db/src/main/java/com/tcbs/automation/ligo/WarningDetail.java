package com.tcbs.automation.ligo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "warning_detail")
public class WarningDetail {

  private static String WARNING_ID = "warningId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "warning_id")
  private Long warningId;
  @NotNull
  @Column(name = "ticker")
  private String ticker;
  @NotNull
  @Column(name = "uuid")
  private String uuid;
  @NotNull
  @Column(name = "created_on")
  private Long createdOn;
  @Column(name = "object_id")
  private String objectId;

  public static List<WarningDetail> getListWarningByWarning(Long warningId) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<WarningDetail> query = session.createQuery("from WarningDetail w where w.warningId =: warningId order by id desc ");
    query.setParameter(WARNING_ID, warningId);
    List<WarningDetail> result = query.getResultList();
    return result;
  }

  public static WarningDetail getListWarningByWarningAndTicker(Long warningId, String ticker) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<WarningDetail> query = session.createQuery("from WarningDetail w where w.warningId =: warningId and w.ticker =: ticker");
    query.setParameter(WARNING_ID, warningId);
    query.setParameter("ticker", ticker);
    return query.getSingleResult();
  }

  public static void deleteByWarningId(Long warningId) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM WarningDetail ib WHERE ib.warningId = :warningId"
    );
    query.setParameter(WARNING_ID, warningId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
