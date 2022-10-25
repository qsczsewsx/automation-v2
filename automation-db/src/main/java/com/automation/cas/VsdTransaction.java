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
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "VSD_TRANSACTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VsdTransaction {
  private static Logger logger = LoggerFactory.getLogger(VsdTransaction.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "PRODUCT")
  private String product;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "STEP")
  private String step;
  @Column(name = "ERR_CODE")
  private String errCode;
  @Column(name = "ERR_MSG")
  private String errMsg;
  @Column(name = "CHANNEL")
  private String channel;
  @Column(name = "RETRY_COUNT")
  private BigDecimal retryCount;
  @Column(name = "PAYLOAD")
  private String payload;
  @Column(name = "ERR_TYPE")
  private BigDecimal errType;
  @Column(name = "SUB_TYPE")
  private String subType;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "TRANSACTION_CODE")
  private String transactionCode;

  @Step
  public static VsdTransaction getById(String id) {
    CAS.casConnection.getSession().clear();
    Query<VsdTransaction> query = CAS.casConnection.getSession().createQuery(
      "from VsdTransaction a where a.id=:id", VsdTransaction.class);
    query.setParameter("id", new BigDecimal(id));
    return query.getSingleResult();
  }

  @Step
  public static VsdTransaction getLastBy105C(String username) {
    CAS.casConnection.getSession().clear();
    Query<VsdTransaction> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT st.* FROM VSD_TRANSACTION st\n" +
        "INNER JOIN xxxx_USER tu \n" +
        "ON st.USER_ID = tu.ID \n" +
        "WHERE tu.USERNAME =:username\n" +
        "ORDER BY st.CREATED_DATE DESC \n" +
        "FETCH FIRST 1 ROWS ONLY", VsdTransaction.class);
    query.setParameter("username", username);
    return query.getSingleResult();
  }

  @Step
  public static void updateStatus(String userId, String status) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query<VsdTransaction> query = CAS.casConnection.getSession().createQuery(
      "update VsdTransaction a set a.status =: status, a.transactionCode = null where a.userId=: userId");
    query.setParameter("status", new BigDecimal(status));
    query.setParameter("userId", new BigDecimal(userId));
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void updateStatusByList(List<String> code105CList, String status) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query<VsdTransaction> query = CAS.casConnection.getSession().createNativeQuery(
      "UPDATE VSD_TRANSACTION SET STATUS = ?1 WHERE USER_ID IN (SELECT ID FROM xxxx_USER WHERE USERNAME IN (?2))");
    query.setParameter(1, new BigDecimal(status));
    query.setParameter(2, code105CList);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void deleteByList(List<String> code105CList) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query<VsdTransaction> query = CAS.casConnection.getSession().createNativeQuery(
      "DELETE FROM VSD_TRANSACTION WHERE USER_ID IN (SELECT ID FROM xxxx_USER WHERE USERNAME IN (?1))");
    query.setParameter(1, code105CList);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static void updateStatusByListIds(List<Integer> id, int status) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query<VsdTransaction> query = CAS.casConnection.getSession().createNativeQuery(
      "UPDATE VSD_TRANSACTION SET STATUS = ?1 WHERE ID IN (?2)");
    query.setParameter(1, new BigDecimal(status));
    query.setParameter(2, id);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static List<VsdTransaction> getByxxxxId(String xxxxId) {
    CAS.casConnection.getSession().clear();
    Query<VsdTransaction> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT st.* FROM VSD_TRANSACTION st\n" +
        "INNER JOIN xxxx_USER tu \n" +
        "ON st.USER_ID = tu.ID \n" +
        "WHERE tu.xxxxID =:xxxxId\n", VsdTransaction.class);
    query.setParameter("xxxxId", xxxxId);
    return query.getResultList();
  }

  @Step
  public static void deleteByUserId(List<String> xxxxId) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query<VsdTransaction> query = CAS.casConnection.getSession().createNativeQuery(
      "DELETE FROM VSD_TRANSACTION WHERE USER_ID IN (SELECT ID FROM xxxx_USER WHERE xxxxID IN (?1))");
    query.setParameter(1, xxxxId);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }
}
