package com.tcbs.automation.ixu;

import lombok.Data;
import lombok.NoArgsConstructor;
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
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "LIMIT_TOTAL_FEE_OF_TCBS_ID")
public class LimitTotalFeeOfTcbsId {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CATEGORY")
  private String category;

  @Column(name = "MAX_AMOUNT")
  private String maxAmount;

  @Column(name = "TOTAL_USED")
  private String totalUsed;

  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "LAST_MODIFY_DATE")
  private Date lastModifyDate;

  private static final String TCBS_ID = "tcbsId";
  private static final String CATEGORY_VALUE = "category";


  @Step
  public static void insert(LimitTotalFeeOfTcbsId entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearByTcbsIds(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession()
      .createQuery("delete from LimitTotalFeeOfTcbsId a where a.tcbsId in :tcbsIds");
    query.setParameter("tcbsIds", tcbsIds);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<LimitTotalFeeOfTcbsId> getListWith(String tcbsId, Timestamp issuedDate, String category) {
    ixuDbConnection.getSession().clear();
    Query<LimitTotalFeeOfTcbsId> query =
      ixuDbConnection
        .getSession()
        .createQuery(
          "from LimitTotalFeeOfTcbsId a where a.tcbsId = :tcbsId and a.category = :category and a.issuedDate = :issuedDate",
          LimitTotalFeeOfTcbsId.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("issuedDate", issuedDate);
    query.setParameter(CATEGORY_VALUE, category);
    List<LimitTotalFeeOfTcbsId> limitList = query.getResultList();
    ixuDbConnection.closeSession();
    return limitList;
  }

  @Step
  public static List<LimitTotalFeeOfTcbsId> findByTcbsIdAndCategoryFromDateToDate(String tcbsId, String category, String fromDate, String toDate) {
    ixuDbConnection.getSession().clear();
    Query<LimitTotalFeeOfTcbsId> query =
      ixuDbConnection
        .getSession()
        .createQuery(
          "from LimitTotalFeeOfTcbsId a where a.tcbsId = :tcbsId and a.category = :category " +
            "and trunc(a.issuedDate) >= to_date(:fromDate, 'yyyy-mm-dd') and trunc(a.issuedDate) <= to_date(:toDate, 'yyyy-mm-dd')",
          LimitTotalFeeOfTcbsId.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(CATEGORY_VALUE, category);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    List<LimitTotalFeeOfTcbsId> limitList = query.getResultList();
    ixuDbConnection.closeSession();
    return limitList;
  }

  @Step
  public static List<LimitTotalFeeOfTcbsId> findByTcbsIdAndCategoryAndIssuedDate(String tcbsId, String category, String issuedDate) {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }
    Query<LimitTotalFeeOfTcbsId> query = ixuDbConnection.getSession().createQuery(
      "from LimitTotalFeeOfTcbsId a where a.tcbsId = :tcbsId and a.category = :category and trunc(a.issuedDate) = to_date(:issuedDate, 'yyyy-mm-dd')",
      LimitTotalFeeOfTcbsId.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(CATEGORY_VALUE, category);
    query.setParameter("issuedDate", issuedDate);
    List<LimitTotalFeeOfTcbsId> limitList = query.getResultList();
    ixuDbConnection.closeSession();
    return limitList;
  }
}
