package com.tcbs.automation.portfolio;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CONFIRMING_DETAIL")
public class ConfirmingDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "SOURCE_ID")
  private String sourceId;
  @Column(name = "CONFIRM_DATE")
  private Date confirmDate;
  @Column(name = "VOLUME")
  private Double volume;

  public static void insertConfirmDetailInfo(String id, String source, String sourceId, String confirmDate, String volume) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();

    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(
      String.format("INSERT INTO CONFIRMING_DETAIL (ID, SOURCE, SOURCE_ID, CONFIRM_DATE, VOLUME) VALUES ('%s', '%s', '%s', TO_DATE('%s','yyyy-MM-dd'), '%s')", id, source, sourceId, confirmDate, volume));
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<ConfirmingDetail> getBySourceAndConfirmDate(String source, String confirmDate) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<ConfirmingDetail> query = session.createQuery("from ConfirmingDetail cd where cd.source =: source and cd.confirmDate =: confirmDate order by id desc");
    query.setParameter("source", source);
    query.setParameter("confirmDate", confirmDate);
    List<ConfirmingDetail> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static ConfirmingDetail getBySourceIdAndConfirmDate(String sourceId, String confirmDate) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<ConfirmingDetail> query = session.createQuery("from ConfirmingDetail cd where cd.sourceId =: sourceId and cd.confirmDate = to_date(:confirmDate,'yyyy-MM-dd') order by id desc");
    query.setParameter("sourceId", sourceId);
    query.setParameter("confirmDate", confirmDate);
    List<ConfirmingDetail> result = query.getResultList();

    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static void deleteBySourceId(String sourceId) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ConfirmingDetail> query = session.createQuery(
      "DELETE FROM ConfirmingDetail cd WHERE cd.sourceId =:sourceId"
    );
    query.setParameter("sourceId", sourceId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
